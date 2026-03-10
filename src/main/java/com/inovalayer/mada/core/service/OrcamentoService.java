package com.inovalayer.mada.core.service;

import com.inovalayer.mada.core.domain.ArameMetalico;
import com.inovalayer.mada.core.domain.Cliente;
import com.inovalayer.mada.core.domain.Orcamento;
import com.inovalayer.mada.core.domain.ParametroGlobal;
import com.inovalayer.mada.core.repository.ArameMetalicoRepository;
import com.inovalayer.mada.core.repository.ClienteRepository;
import com.inovalayer.mada.core.repository.OrcamentoRepository;
import com.inovalayer.mada.core.repository.ParametroGlobalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID; // Correção: Importação vital para o reconhecimento do tipo UUID

/**
 * Serviço central do projeto MADA.
 * Orquestra dados de múltiplas tabelas para gerar o custo final da Peça Única.
 */
@Service
@RequiredArgsConstructor
public class OrcamentoService {

    private final OrcamentoRepository orcamentoRepository;
    private final ClienteRepository clienteRepository;
    private final ArameMetalicoRepository arameRepository;
    private final ParametroGlobalRepository parametroRepository;

    /**
     * Gera um novo orçamento baseado nos dados crus vindos do Python e consolida os custos.
     */
    @Transactional
    public Orcamento gerarOrcamento(UUID clienteId, UUID arameId, BigDecimal tempoArcoMinutos, BigDecimal massaEstimadaKg) {
        
        // 1. Validar a existência das entidades na base de dados
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado com o ID fornecido."));
        
        ArameMetalico arame = arameRepository.findById(arameId)
                .orElseThrow(() -> new IllegalArgumentException("Arame Metálico não encontrado com o ID fornecido."));

        // Busca o primeiro parâmetro global configurado (assumindo que só existe uma linha ativa de taxas)
        ParametroGlobal parametros = parametroRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("Taxas globais não configuradas no sistema."));

        // 2. Instanciar o novo orçamento
        Orcamento novoOrcamento = new Orcamento();
        novoOrcamento.setCliente(cliente);
        novoOrcamento.setArameMetalico(arame);
        novoOrcamento.setTempoArcoAbertoMinutos(tempoArcoMinutos);
        novoOrcamento.setMassaEstimadaKg(massaEstimadaKg);

        // 3. REGRA DE NEGÓCIO: Snapshotting (Congelamento de preços históricos)
        novoOrcamento.setSnapPrecoKgArameAplicado(arame.getPrecoUnitarioBase());
        novoOrcamento.setSnapCustoKwhAplicado(parametros.getCustoKwh());

        // 4. REGRA DE NEGÓCIO: O Cálculo Matemático (Core do LAPROSOLDA)
        // Custo Material = Massa (kg) * Preço do Arame (R$/kg)
        BigDecimal custoMaterial = massaEstimadaKg.multiply(novoOrcamento.getSnapPrecoKgArameAplicado());
        
        // Custo Total (Provisório MVP) = Custo Material * Margem de Lucro
        BigDecimal custoTotal = custoMaterial.multiply(parametros.getMargemLucroPercentual());
        
        novoOrcamento.setCustoTotalCalculado(custoTotal);
        novoOrcamento.setStatusOrcamento("PENDENTE");

        // 5. Persistir na base de dados
        return orcamentoRepository.save(novoOrcamento);
    }
}