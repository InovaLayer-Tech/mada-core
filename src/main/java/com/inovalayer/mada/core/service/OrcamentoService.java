package com.inovalayer.mada.core.service;

import com.inovalayer.mada.core.domain.*;
import com.inovalayer.mada.core.dto.OrcamentoRequestDTO;
import com.inovalayer.mada.core.dto.OrcamentoResponseDTO;
import com.inovalayer.mada.core.mapper.OrcamentoMapper;
import com.inovalayer.mada.core.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service orquestrador das regras de negócio metrológico-financeiras (Metodologia WAAM).
 */
@Service
@RequiredArgsConstructor
public class OrcamentoService {

    private final OrcamentoRepository orcamentoRepository;
    private final ArameMetalicoRepository arameMetalicoRepository;
    private final GasProtecaoRepository gasProtecaoRepository;
    private final ParametroGlobalRepository parametroGlobalRepository;
    private final OrcamentoMapper orcamentoMapper;

    @Transactional
    public OrcamentoResponseDTO criarOrcamento(OrcamentoRequestDTO request) {
        // 1. Buscas de Dados SSOT (Fail-Fast)
        ArameMetalico arame = arameMetalicoRepository.findById(request.arameId())
                .orElseThrow(() -> new EntityNotFoundException("Arame metálico não encontrado."));

        // Busca o gás ativo padrão
        GasProtecao gas = gasProtecaoRepository.findAll().stream()
                .filter(GasProtecao::getAtivo)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Nenhum gás de proteção ativo disponível."));

        ParametroGlobal params = parametroGlobalRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Parâmetros globais não configurados."));

        // 2. Orquestração das Fases
        Orcamento orcamento = new Orcamento();
        preencherDadosFisicos(orcamento, request);

        OrcamentoIC ic = calcularFaseIC(request, params);
        OrcamentoDC dc = calcularFaseDC(request, arame, gas, params);
        List<OrcamentoAC> acList = calcularFaseAC(request, params, orcamento);

        // 3. Consolidação e Persistência
        orcamento.setFase1IC(ic);
        orcamento.setFase2DC(dc);
        orcamento.setFase3AC(acList);
        orcamento.setStatus(StatusOrcamento.PENDENTE);

        BigDecimal custoTotalFases = ic.getCustoTotalIC()
                .add(dc.getCustoTotalDC())
                .add(orcamento.getFase3AC().stream()
                        .map(OrcamentoAC::getCustoTotalAC)
                        .reduce(BigDecimal.ZERO, BigDecimal::add));

        orcamento.setCustoTotalFinal(custoTotalFases.setScale(2, RoundingMode.HALF_UP));

        Orcamento saved = orcamentoRepository.save(orcamento);
        return orcamentoMapper.toDto(saved);
    }

    private void preencherDadosFisicos(Orcamento orcamento, OrcamentoRequestDTO request) {
        orcamento.setQuantidade(request.quantidade());
        orcamento.setDimensaoX(request.dimensaoX());
        orcamento.setDimensaoY(request.dimensaoY());
        orcamento.setDimensaoZ(request.dimensaoZ());
        orcamento.setTolerancia(request.tolerancia());
        orcamento.setAcabamento(request.acabamento());
        orcamento.setNivelInspecao(request.nivelInspecao());
        orcamento.setTratamentoTermico(request.tratamentoTermico());
    }

    private OrcamentoIC calcularFaseIC(OrcamentoRequestDTO request, ParametroGlobal params) {
        OrcamentoIC ic = new OrcamentoIC();
        BigDecimal qtd = BigDecimal.valueOf(request.quantidade());

        // Snapshots
        ic.setTaxaMaoDeObraSnapshot(params.getTaxaMaoDeObraHora());
        ic.setTaxaEngenheiroSnapshot(params.getTaxaEngenheiroHora());
        ic.setQuantidadeLoteSnapshot(request.quantidade());

        // Custo Setup Processo: (Tempo Setup * Taxa Mão Obra) / Quantidade
        BigDecimal setupFixo = BigDecimal.valueOf(request.tempoPreparacaoMinutos())
                .divide(new BigDecimal("60"), 6, RoundingMode.HALF_UP)
                .multiply(params.getTaxaMaoDeObraHora());
        ic.setCustoSetupProcesso(setupFixo.divide(qtd, 2, RoundingMode.HALF_UP));

        // Custo Engenharia: (Engenharia CAD/CAM * Taxa Engenheiro) / Quantidade
        // Assumindo 1h de engenharia como base se não estiver no DTO
        BigDecimal engFixo = new BigDecimal("1.0").multiply(params.getTaxaEngenheiroHora());
        ic.setCustoEngenharia(engFixo.divide(qtd, 2, RoundingMode.HALF_UP));

        ic.setTempoPreparacaoMinutos(request.tempoPreparacaoMinutos());
        ic.setTempoRemocaoMinutos(request.tempoRemocaoMinutos());
        ic.setCustoSubstrato(BigDecimal.ZERO); // Futuro
        ic.setCustoPreparacao(ic.getCustoSetupProcesso());
        
        BigDecimal remoFixo = BigDecimal.valueOf(request.tempoRemocaoMinutos())
                .divide(new BigDecimal("60"), 6, RoundingMode.HALF_UP)
                .multiply(params.getTaxaMaoDeObraHora());
        ic.setCustoRemocao(remoFixo.divide(qtd, 2, RoundingMode.HALF_UP));

        ic.setCustoTotalIC(ic.getCustoSubstrato().add(ic.getCustoSetupProcesso())
                .add(ic.getCustoEngenharia()).add(ic.getCustoRemocao()));
        
        return ic;
    }

    private OrcamentoDC calcularFaseDC(OrcamentoRequestDTO request, ArameMetalico arame, GasProtecao gas, ParametroGlobal params) {
        OrcamentoDC dc = new OrcamentoDC();
        dc.setArameMetalico(arame);
        dc.setGasProtecao(gas);
        
        // Snapshot
        dc.setAramePrecoKgSnapshot(arame.getPrecoUnitarioBase());
        dc.setArameDensidadeSnapshot(arame.getDensidadeGcm3());
        dc.setArameEficienciaSnapshot(arame.getEficiencia());
        dc.setGasVazaoSnapshot(gas.getVazaoPadrao());
        dc.setGasPrecoM3Snapshot(gas.getPrecoUnitarioBase());
        dc.setTaxaEnergiaSnapshot(params.getCustoKwh());
        dc.setTaxaMaquinaSnapshot(params.getTaxaDepreciacaoMaquinaHora());

        // Cm = (Massa Estimada / (Eficiencia / 100)) * Preço
        BigDecimal massaTeorica = BigDecimal.valueOf(request.massaEstimadaKg());
        BigDecimal ef = BigDecimal.valueOf(dc.getArameEficienciaSnapshot()).divide(new BigDecimal("100"), 6, RoundingMode.HALF_UP);
        BigDecimal massaReal = massaTeorica.divide(ef, 6, RoundingMode.HALF_UP);
        dc.setCustoMaterial(massaReal.multiply(dc.getAramePrecoKgSnapshot()).setScale(2, RoundingMode.HALF_UP));

        // Cg = (TempoArco * Vazao) * PreçoLitro
        BigDecimal volLitros = BigDecimal.valueOf(request.tempoArcoMinutos()).multiply(BigDecimal.valueOf(dc.getGasVazaoSnapshot()));
        BigDecimal precoL = dc.getGasPrecoM3Snapshot().divide(new BigDecimal("1000"), 6, RoundingMode.HALF_UP);
        dc.setCustoGas(volLitros.multiply(precoL).setScale(2, RoundingMode.HALF_UP));

        // Energia e Máquina
        BigDecimal horas = BigDecimal.valueOf(request.tempoArcoMinutos()).divide(new BigDecimal("60"), 6, RoundingMode.HALF_UP);
        dc.setCustoEnergia(horas.multiply(new BigDecimal("5.5")).multiply(dc.getTaxaEnergiaSnapshot()).setScale(2, RoundingMode.HALF_UP));
        dc.setCustoMaquina(horas.multiply(dc.getTaxaMaquinaSnapshot()).setScale(2, RoundingMode.HALF_UP));

        dc.setTempoArcoMinutos(request.tempoArcoMinutos());
        dc.setMassaEstimadaKg(request.massaEstimadaKg());
        dc.setCustoTotalDC(dc.getCustoMaterial().add(dc.getCustoGas()).add(dc.getCustoEnergia()).add(dc.getCustoMaquina()));

        return dc;
    }

    private List<OrcamentoAC> calcularFaseAC(OrcamentoRequestDTO request, ParametroGlobal params, Orcamento orcamento) {
        List<OrcamentoAC> acs = new ArrayList<>();

        if (Boolean.TRUE.equals(request.tratamentoTermico())) {
            OrcamentoAC tt = new OrcamentoAC();
            tt.setOrcamento(orcamento);
            tt.setDescricaoServico("Tratamento Térmico de Alívio de Tensão");
            tt.setQuantidadeHoras(1.0);
            tt.setTaxaAplicadaSnapshot(params.getCustoTratamentoTermicoFixo());
            tt.setCustoTotalAC(params.getCustoTratamentoTermicoFixo());
            acs.add(tt);
        }

        String acab = request.acabamento();
        if ("Faceado".equalsIgnoreCase(acab) || "Usinagem Fina".equalsIgnoreCase(acab)) {
            OrcamentoAC cnc = new OrcamentoAC();
            cnc.setOrcamento(orcamento);
            cnc.setDescricaoServico("Usinagem CNC de Acabamento (" + acab + ")");
            Double min = request.tempoUsinagemMinutos() != null ? request.tempoUsinagemMinutos() : 60.0;
            BigDecimal h = BigDecimal.valueOf(min).divide(new BigDecimal("60"), 6, RoundingMode.HALF_UP);
            cnc.setQuantidadeHoras(h.doubleValue());
            cnc.setTaxaAplicadaSnapshot(params.getTaxaUsinagemHora());
            cnc.setCustoTotalAC(h.multiply(params.getTaxaUsinagemHora()).setScale(2, RoundingMode.HALF_UP));
            acs.add(cnc);
        }

        return acs;
    }

    @Transactional(readOnly = true)
    public OrcamentoResponseDTO buscarPorId(UUID id) {
        Orcamento orcamento = orcamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Orçamento não encontrado."));
        return orcamentoMapper.toDto(orcamento);
    }
}