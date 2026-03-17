package com.inovalayer.mada.core.domain;

/**
 * Enumerador blindando o ciclo de vida de um orçamento WAAM.
 */
public enum StatusOrcamento {
    PENDENTE,      // Cliente solicitou, aguardando dados da engenharia.
    CALCULADO,     // Engenharia processou massa/tempo e custos foram gerados.
    APROVADO,      // Cliente aprovou o custo, liberado para produção.
    REJEITADO,     // Orçamento descartado.
    EM_EXECUCAO,   // Célula robótica em operação.
    FINALIZADO     // Peça pronta.
}