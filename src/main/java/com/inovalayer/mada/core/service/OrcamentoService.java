package com.inovalayer.mada.core.service;

import com.inovalayer.mada.core.domain.ArameMetalico;
import com.inovalayer.mada.core.domain.Orcamento;
import com.inovalayer.mada.core.domain.StatusOrcamento;
import com.inovalayer.mada.core.dto.OrcamentoRequestDTO;
import com.inovalayer.mada.core.repository.ArameMetalicoRepository; // Assumindo que você já tem esta interface
import com.inovalayer.mada.core.repository.OrcamentoRepository;     // Assumindo que você já tem esta interface
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Criei este Service como o orquestrador das regras de negócio.
 * Ele é responsável por aplicar o cálculo metrológico-financeiro, garantindo a
 * integridade transacional (ACID) antes de persistir o documento no PostgreSQL.
 */
@Service
@RequiredArgsConstructor
public class OrcamentoService {

    // A injeção via construtor com variáveis 'final' garante a imutabilidade do serviço.
    private final OrcamentoRepository orcamentoRepository;
    private final ArameMetalicoRepository arameMetalicoRepository;

    // Constante temporária de custo hora-máquina (R$ 150,00).
    // Conforme o nosso Diagrama de Classes, no futuro buscaremos isto da tabela ConfiguracaoSistema.
    private static final BigDecimal TAXA_HORA_MAQUINA = new BigDecimal("150.00");

    @Transactional
    public Orcamento processarMetrologia(OrcamentoRequestDTO request) {
        
        // 1. Busca Segura (Fail-Fast)
        ArameMetalico arame = arameMetalicoRepository.findById(request.arameId())
                .orElseThrow(() -> new IllegalArgumentException("O insumo selecionado não existe no catálogo."));

        // 2. Conversão de Grandezas
        BigDecimal massa = BigDecimal.valueOf(request.massaEstimadaKg());
        BigDecimal tempoMinutos = BigDecimal.valueOf(request.tempoArcoMinutos());

        // 3. Regra de Negócio Financeira (Matemática de Precisão)
        
        // Custo Material = Massa (kg) * Preço Unitário (R$/kg)
        BigDecimal custoMaterial = arame.getPrecoUnitarioBase().multiply(massa)
                .setScale(2, RoundingMode.HALF_UP);

        // Custo Operacional = (Tempo em Minutos / 60) * Taxa da Máquina (R$/h)
        // Uso escala 6 na divisão para evitar a ArithmeticException (dízimas periódicas)
        BigDecimal horasOperacao = tempoMinutos.divide(new BigDecimal("60"), 6, RoundingMode.HALF_UP);
        BigDecimal custoOperacional = horasOperacao.multiply(TAXA_HORA_MAQUINA)
                .setScale(2, RoundingMode.HALF_UP);

        // Custo Total = Material + Operacional
        BigDecimal custoTotal = custoMaterial.add(custoOperacional);

        // 4. Montagem do Documento (State Machine)
        Orcamento orcamento = new Orcamento();
        orcamento.setArameMetalico(arame);
        orcamento.setTempoArcoMinutos(request.tempoArcoMinutos());
        orcamento.setMassaEstimadaKg(request.massaEstimadaKg());
        orcamento.setCustoMaterial(custoMaterial);
        orcamento.setCustoOperacional(custoOperacional);
        orcamento.setCustoTotalFinal(custoTotal);
        orcamento.setStatus(StatusOrcamento.PENDENTE); // Status inicial blindado pelo Enum

        // 5. Persistência
        return orcamentoRepository.save(orcamento);
    }
}