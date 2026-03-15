package com.inovalayer.mada.core.service;

import com.inovalayer.mada.core.domain.*;
import com.inovalayer.mada.core.dto.OrcamentoRequestDTO;
import com.inovalayer.mada.core.dto.OrcamentoResponseDTO;
import com.inovalayer.mada.core.mapper.OrcamentoAuditMapper;
import com.inovalayer.mada.core.repository.ArameMetalicoRepository;
import com.inovalayer.mada.core.repository.OrcamentoRepository;
import com.inovalayer.mada.core.repository.ParametroGlobalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service orquestrador das regras de negócio metrológico-financeiras.
 * Atualizado para as diretrizes da Sprint 2 com suporte pleno a IC, DC e AC
 * e proteção de carga útil através do cruzamento com SSOT (ParametroGlobal).
 */
@Service
@RequiredArgsConstructor
public class OrcamentoService {

    private final OrcamentoRepository orcamentoRepository;
    private final ArameMetalicoRepository arameMetalicoRepository;
    private final ParametroGlobalRepository parametroGlobalRepository;
    private final OrcamentoAuditMapper auditMapper;

    // TODO: Num nível mais maduro, a taxa da máquina estará atrelada à Entidade Equipamento.
    // Para simplificação de setup Sprint 2, mantemos hardcoded se não houver um campo no ParametroGlobal.
    private static final BigDecimal TAXA_HORA_MAQUINA = new BigDecimal("150.00");

    @Transactional
    public OrcamentoResponseDTO processarMetrologia(OrcamentoRequestDTO request) {
        
        // 1. Buscas de Dados SSOT (Fail-Fast)
        ArameMetalico arame = arameMetalicoRepository.findById(request.arameId())
                .orElseThrow(() -> new IllegalArgumentException("O insumo selecionado não existe no catálogo."));

        List<ParametroGlobal> parametros = parametroGlobalRepository.findAll();
        if (parametros.isEmpty()) {
            throw new IllegalStateException("Os parâmetros globais do sistema ainda não foram inicializados pelo Administrador.");
        }
        ParametroGlobal params = parametros.get(0);

        // Conversões de Grandezas (Evitando casts repetitivos)
        BigDecimal tempoPrep = BigDecimal.valueOf(request.tempoPreparacaoMinutos());
        BigDecimal tempoRemo = BigDecimal.valueOf(request.tempoRemocaoMinutos());
        BigDecimal tempoArco = BigDecimal.valueOf(request.tempoArcoMinutos());
        BigDecimal massa = BigDecimal.valueOf(request.massaEstimadaKg());

        // -----------------------------------------------------------------------
        // FASE 1: Initial Costs (IC)
        // -----------------------------------------------------------------------
        BigDecimal taxaMaoObraMinuto = params.getTaxaMaoDeObraHora().divide(new BigDecimal("60"), 6, RoundingMode.HALF_UP);
        
        OrcamentoIC ic = new OrcamentoIC();
        ic.setTempoPreparacaoMinutos(request.tempoPreparacaoMinutos());
        ic.setTempoRemocaoMinutos(request.tempoRemocaoMinutos());
        ic.setTaxaMaoDeObraSnapshot(params.getTaxaMaoDeObraHora()); // Snapshot da hora

        ic.setCustoSubstrato(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)); // Stub até termos CRUD de Substratos
        BigDecimal custoPrep = tempoPrep.multiply(taxaMaoObraMinuto).setScale(2, RoundingMode.HALF_UP);
        ic.setCustoPreparacao(custoPrep);
        BigDecimal custoRemo = tempoRemo.multiply(taxaMaoObraMinuto).setScale(2, RoundingMode.HALF_UP);
        ic.setCustoRemocao(custoRemo);

        BigDecimal totalIC = ic.getCustoSubstrato().add(custoPrep).add(custoRemo);
        ic.setCustoTotalIC(totalIC);

        // -----------------------------------------------------------------------
        // FASE 2: Deposition Costs (DC)
        // -----------------------------------------------------------------------
        OrcamentoDC dc = new OrcamentoDC();
        dc.setArameMetalico(arame);
        dc.setTempoArcoMinutos(request.tempoArcoMinutos());
        dc.setMassaEstimadaKg(request.massaEstimadaKg());
        
        // Snapshotting das regras voláteis
        dc.setAramePrecoKgSnapshot(arame.getPrecoUnitarioBase());
        dc.setGasPrecoLitroSnapshot(BigDecimal.ZERO); // Stub
        dc.setTaxaEnergiaSnapshot(params.getCustoKwh());
        dc.setTaxaMaquinaSnapshot(TAXA_HORA_MAQUINA); 

        // Cálculo Core (Cm + Cg + Ce + Cmq)
        BigDecimal custoMaterial = arame.getPrecoUnitarioBase().multiply(massa).setScale(2, RoundingMode.HALF_UP);
        dc.setCustoMaterial(custoMaterial);
        dc.setCustoGas(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        dc.setCustoEnergia(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)); // Stub Potência
        
        BigDecimal horasOperacao = tempoArco.divide(new BigDecimal("60"), 6, RoundingMode.HALF_UP);
        BigDecimal custoMaquina = horasOperacao.multiply(dc.getTaxaMaquinaSnapshot()).setScale(2, RoundingMode.HALF_UP);
        dc.setCustoMaquina(custoMaquina);

        BigDecimal totalDC = custoMaterial.add(dc.getCustoGas()).add(dc.getCustoEnergia()).add(custoMaquina);
        dc.setCustoTotalDC(totalDC);

        // -----------------------------------------------------------------------
        // FASE 3: Additional Costs (AC)
        // -----------------------------------------------------------------------
        List<OrcamentoAC> listaAC = new ArrayList<>();
        BigDecimal totalAC = BigDecimal.ZERO;

        if (Boolean.TRUE.equals(request.requerProjetoCAD())) {
            OrcamentoAC acCAD = new OrcamentoAC();
            acCAD.setDescricaoServico("Implementação CAD/CAM e Fatiamento Python");
            acCAD.setQuantidadeHoras(1.0); // Engenharia fixa como 1h por enquanto
            acCAD.setTaxaAplicadaSnapshot(params.getTaxaMaoDeObraHora());
            
            BigDecimal custoAC = params.getTaxaMaoDeObraHora().multiply(new BigDecimal("1.0")).setScale(2, RoundingMode.HALF_UP);
            acCAD.setCustoTotalAC(custoAC);
            listaAC.add(acCAD);
            totalAC = totalAC.add(custoAC);
        }

        if (Boolean.TRUE.equals(request.requerUsinagemFinal()) && request.tempoUsinagemMinutos() != null) {
            OrcamentoAC acUsinagem = new OrcamentoAC();
            acUsinagem.setDescricaoServico("Usinagem CNC Final");
            
            Double minUsinagem = request.tempoUsinagemMinutos();
            acUsinagem.setQuantidadeHoras(minUsinagem / 60.0);
            acUsinagem.setTaxaAplicadaSnapshot(TAXA_HORA_MAQUINA);
            
            BigDecimal horasUsina = BigDecimal.valueOf(minUsinagem).divide(new BigDecimal("60"), 6, RoundingMode.HALF_UP);
            BigDecimal custoAC = horasUsina.multiply(TAXA_HORA_MAQUINA).setScale(2, RoundingMode.HALF_UP);
            acUsinagem.setCustoTotalAC(custoAC);
            
            listaAC.add(acUsinagem);
            totalAC = totalAC.add(custoAC);
        }

        // -----------------------------------------------------------------------
        // CONSOLIDAÇÃO (Aggregate Root)
        // -----------------------------------------------------------------------
        Orcamento orcamento = new Orcamento();
        orcamento.setStatus(StatusOrcamento.PENDENTE); // Status imutável da web
        
        orcamento.setFase1IC(ic);
        orcamento.setFase2DC(dc);
        
        for (OrcamentoAC ac : listaAC) {
            ac.setOrcamento(orcamento); // Bind bidirecional para JPA/Hibernate
        }
        orcamento.setFase3AC(listaAC);

        // Equação Máster: CT = IC + DC + AC
        BigDecimal custoTotalFinal = totalIC.add(totalDC).add(totalAC).setScale(2, RoundingMode.HALF_UP);
        orcamento.setCustoTotalFinal(custoTotalFinal);

        // Salva a estrutura cascateada inteira num único commit ACID
        Orcamento saved = orcamentoRepository.save(orcamento);

        // Mapeia para o DTO e devolve para a Web
        return auditMapper.toAuditDto(saved);
    }

    @Transactional(readOnly = true)
    public OrcamentoResponseDTO buscarPorId(UUID id) {
        Orcamento orcamento = orcamentoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Orçamento não encontrado com o ID fornecido."));
        return auditMapper.toAuditDto(orcamento);
    }
}