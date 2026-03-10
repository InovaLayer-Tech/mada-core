package com.inovalayer.mada.core.dto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Projetei este DTO utilizando a estrutura 'record' introduzida nas versões recentes do Java.
 * Ele atua como um contrato de rede estrito (API Contract) entre o Back-end e o Angular.
 * Ele achata a hierarquia do banco de dados, entregando num único pacote os dados da tabela
 * pai (Consumível) e da tabela filha (Arame).
 */
public record ArameMetalicoResponseDTO(
        UUID id,
        
        // Mapeamento Comercial (Tabela Consumivel)
        // Mantive a nomenclatura 'nomeFabricante' para respeitar o contrato existente no HTML do Angular.
        String nomeFabricante, 
        String codigoProduto,
        BigDecimal precoUnitarioBase,
        
        // Mapeamento Físico (Tabela ArameMetalico)
        String ligaMetalica,
        Double diametroMm,
        Double densidadeGcm3
) {}