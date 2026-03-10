package com.inovalayer.mada.core.domain;

/**
 * Criei este enumerador para blindar o ciclo de vida de um orçamento.
 * Ao invés de usar Strings soltas ("Pendente", "pendente", "PENDENTE"), 
 * o Enum força o sistema, em tempo de compilação, a aceitar apenas estes 4 estados exatos,
 * eliminando erros de digitação e inconsistências no banco de dados.
 */
public enum StatusOrcamento {
    PENDENTE,      // Orçamento gerado, aguardando aprovação do cliente.
    APROVADO,      // Cliente aprovou o custo, liberado para a engenharia.
    EM_EXECUCAO,   // Robô está ativamente soldando a peça baseada neste orçamento.
    FINALIZADO     // Peça concluída, dados prontos para a IA analisar o Desvio Previsto vs Realizado.
}