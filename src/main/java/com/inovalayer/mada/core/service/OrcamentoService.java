package com.inovalayer.mada.core.service;

import com.inovalayer.mada.core.domain.*;
import com.inovalayer.mada.core.dto.OrcamentoCalculoRequestDTO;
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
 * Refatorado para suportar o fluxo segregado: Solicitação (B2C) -> Cálculo (B2B).
 */
@Service
@RequiredArgsConstructor
public class OrcamentoService {

    private final OrcamentoRepository orcamentoRepository;
    private final ArameMetalicoRepository arameMetalicoRepository;
    private final GasProtecaoRepository gasProtecaoRepository;
    private final ParametroGlobalRepository parametroGlobalRepository;
    private final OrcamentoMapper orcamentoMapper;

    /**
     * Etapa 1: Registro da Solicitação inicial do Cliente (B2C).
     */
    @Transactional
    public OrcamentoResponseDTO solicitarOrcamento(OrcamentoRequestDTO request) {
        Orcamento orcamento = new Orcamento();
        orcamento.setNomeProjeto(request.nomeProjeto());
        orcamento.setQuantidade(request.quantidade());
        orcamento.setDimensaoX(request.dimensaoX());
        orcamento.setDimensaoY(request.dimensaoY());
        orcamento.setDimensaoZ(request.dimensaoZ());
        orcamento.setTolerancia(request.tolerancia());
        orcamento.setAcabamento(request.acabamento());
        orcamento.setTratamentoTermico(request.tratamentoTermico());
        orcamento.setStatus(StatusOrcamento.PENDENTE);

        Orcamento saved = orcamentoRepository.save(orcamento);
        return orcamentoMapper.toDto(saved);
    }

    /**
     * Etapa 2: Processamento Metrológico e Financeiro pela Engenharia (B2B).
     */
    @Transactional
    public OrcamentoResponseDTO processarCalculo(UUID id, OrcamentoCalculoRequestDTO request) {
        Orcamento orcamento = orcamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Orçamento não encontrado."));

        if (orcamento.getStatus() != StatusOrcamento.PENDENTE && orcamento.getStatus() != StatusOrcamento.CALCULADO) {
            throw new IllegalStateException("Apenas orçamentos pendentes podem ser calculados.");
        }

        // 1. Buscas de Dados SSOT
        ArameMetalico arame = arameMetalicoRepository.findById(request.arameId())
                .orElseThrow(() -> new EntityNotFoundException("Arame metálico não encontrado."));

        GasProtecao gas = gasProtecaoRepository.findAll().stream()
                .filter(GasProtecao::getAtivo)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Nenhum gás de proteção ativo disponível."));

        ParametroGlobal params = parametroGlobalRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Parâmetros globais não configurados."));

        // 2. Orquestração das Fases WAAM
        OrcamentoIC ic = calcularFaseIC(request, params, orcamento);
        OrcamentoDC dc = calcularFaseDC(request, arame, gas, params, orcamento);
        List<OrcamentoAC> acList = calcularFaseAC(request, params, orcamento);

        // 3. Consolidação
        orcamento.setFase1IC(ic);
        orcamento.setFase2DC(dc);
        orcamento.getFase3AC().clear();
        orcamento.getFase3AC().addAll(acList);
        orcamento.setStatus(StatusOrcamento.CALCULADO);

        BigDecimal custoTotalFases = ic.getCustoTotalIC()
                .add(dc.getCustoTotalDC())
                .add(acList.stream()
                        .map(OrcamentoAC::getCustoTotalAC)
                        .reduce(BigDecimal.ZERO, BigDecimal::add));

        orcamento.setCustoTotalFinal(custoTotalFases.setScale(2, RoundingMode.HALF_UP));

        // Cálculo do Preço Sugerido (Custo + Margem)
        BigDecimal margem = params.getMargemLucroPercentual().divide(new BigDecimal("100"), 6, RoundingMode.HALF_UP).add(BigDecimal.ONE);
        orcamento.setPrecoFinalSugerido(custoTotalFases.multiply(margem).setScale(2, RoundingMode.HALF_UP));

        Orcamento saved = orcamentoRepository.save(orcamento);
        return orcamentoMapper.toDto(saved);
    }

    private OrcamentoIC calcularFaseIC(OrcamentoCalculoRequestDTO request, ParametroGlobal params, Orcamento orcamento) {
        OrcamentoIC ic = new OrcamentoIC();
        ic.setOrcamento(orcamento);
        BigDecimal qtd = BigDecimal.valueOf(orcamento.getQuantidade());

        ic.setTaxaMaoDeObraSnapshot(params.getTaxaMaoDeObraHora());
        ic.setTaxaEngenheiroSnapshot(params.getTaxaEngenheiroHora());
        ic.setQuantidadeLoteSnapshot(orcamento.getQuantidade());

        BigDecimal setupFixo = BigDecimal.valueOf(request.tempoPreparacaoMinutos())
                .divide(new BigDecimal("60"), 6, RoundingMode.HALF_UP)
                .multiply(params.getTaxaMaoDeObraHora());
        ic.setCustoSetupProcesso(setupFixo.divide(qtd, 2, RoundingMode.HALF_UP));

        BigDecimal engFixo = new BigDecimal("1.0").multiply(params.getTaxaEngenheiroHora());
        ic.setCustoEngenharia(engFixo.divide(qtd, 2, RoundingMode.HALF_UP));

        ic.setTempoPreparacaoMinutos(request.tempoPreparacaoMinutos());
        ic.setTempoRemocaoMinutos(request.tempoRemocaoMinutos());
        ic.setCustoSubstrato(BigDecimal.ZERO);
        ic.setCustoPreparacao(ic.getCustoSetupProcesso());
        
        BigDecimal remoFixo = BigDecimal.valueOf(request.tempoRemocaoMinutos())
                .divide(new BigDecimal("60"), 6, RoundingMode.HALF_UP)
                .multiply(params.getTaxaMaoDeObraHora());
        ic.setCustoRemocao(remoFixo.divide(qtd, 2, RoundingMode.HALF_UP));

        ic.setCustoTotalIC(ic.getCustoSubstrato().add(ic.getCustoSetupProcesso())
                .add(ic.getCustoEngenharia()).add(ic.getCustoRemocao()));
        
        return ic;
    }

    private OrcamentoDC calcularFaseDC(OrcamentoCalculoRequestDTO request, ArameMetalico arame, GasProtecao gas, ParametroGlobal params, Orcamento orcamento) {
        OrcamentoDC dc = new OrcamentoDC();
        dc.setOrcamento(orcamento);
        dc.setArameMetalico(arame);
        dc.setGasProtecao(gas);
        
        dc.setAramePrecoKgSnapshot(arame.getPrecoUnitarioBase());
        dc.setArameDensidadeSnapshot(arame.getDensidadeGcm3());
        dc.setArameEficienciaSnapshot(arame.getEficiencia());
        dc.setGasVazaoSnapshot(gas.getVazaoPadrao());
        dc.setGasPrecoM3Snapshot(gas.getPrecoUnitarioBase());
        dc.setTaxaEnergiaSnapshot(params.getCustoKwh());
        dc.setTaxaMaquinaSnapshot(params.getTaxaDepreciacaoMaquinaHora());

        BigDecimal massaTeorica = BigDecimal.valueOf(request.massaEstimadaKg());
        BigDecimal ef = BigDecimal.valueOf(dc.getArameEficienciaSnapshot()).divide(new BigDecimal("100"), 6, RoundingMode.HALF_UP);
        BigDecimal massaReal = massaTeorica.divide(ef, 6, RoundingMode.HALF_UP);
        dc.setCustoMaterial(massaReal.multiply(dc.getAramePrecoKgSnapshot()).setScale(2, RoundingMode.HALF_UP));

        BigDecimal volLitros = BigDecimal.valueOf(request.tempoArcoMinutos()).multiply(BigDecimal.valueOf(dc.getGasVazaoSnapshot()));
        BigDecimal precoL = dc.getGasPrecoM3Snapshot().divide(new BigDecimal("1000"), 6, RoundingMode.HALF_UP);
        dc.setCustoGas(volLitros.multiply(precoL).setScale(2, RoundingMode.HALF_UP));

        BigDecimal horas = BigDecimal.valueOf(request.tempoArcoMinutos()).divide(new BigDecimal("60"), 6, RoundingMode.HALF_UP);
        dc.setCustoEnergia(horas.multiply(params.getConsumoPotenciaKw()).multiply(dc.getTaxaEnergiaSnapshot()).setScale(2, RoundingMode.HALF_UP));
        dc.setCustoMaquina(horas.multiply(dc.getTaxaMaquinaSnapshot()).setScale(2, RoundingMode.HALF_UP));

        dc.setTempoArcoMinutos(request.tempoArcoMinutos());
        dc.setMassaEstimadaKg(request.massaEstimadaKg());
        dc.setCustoTotalDC(dc.getCustoMaterial().add(dc.getCustoGas()).add(dc.getCustoEnergia()).add(dc.getCustoMaquina()));

        return dc;
    }

    private List<OrcamentoAC> calcularFaseAC(OrcamentoCalculoRequestDTO request, ParametroGlobal params, Orcamento orcamento) {
        List<OrcamentoAC> acs = new ArrayList<>();

        if (Boolean.TRUE.equals(request.requerProjetoCAD())) {
            OrcamentoAC tt = new OrcamentoAC();
            tt.setOrcamento(orcamento);
            tt.setDescricaoServico("Desenvolvimento de Projeto CAD/CAM");
            tt.setQuantidadeHoras(1.0);
            tt.setTaxaAplicadaSnapshot(params.getTaxaEngenheiroHora());
            tt.setCustoTotalAC(params.getTaxaEngenheiroHora());
            acs.add(tt);
        }

        if (Boolean.TRUE.equals(request.requerUsinagemFinal())) {
            OrcamentoAC cnc = new OrcamentoAC();
            cnc.setOrcamento(orcamento);
            cnc.setDescricaoServico("Usinagem CNC de Acabamento");
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

    @Transactional(readOnly = true)
    public List<OrcamentoResponseDTO> listarTodos() {
        return orcamentoRepository.findAll()
                .stream()
                .map(orcamentoMapper::toDto)
                .toList();
    }
}