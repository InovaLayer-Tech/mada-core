package com.inovalayer.mada.core.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Entidade abstrata (Pai) que define atributos comuns a qualquer insumo consumido no processo WAAM.
 * Localização: src/main/java/com/inovalayer/mada/core/domain/
 */
@Getter
@Setter
@Entity
@Table(name = "tb_consumivel")
// Estratégia JOINED: O Hibernate criará a tabela 'tb_consumivel' com os campos básicos,
// e tabelas filhas separadas para Arame e Gás, ligadas por Chave Estrangeira (FK).
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Consumivel extends BaseEntity {

    @Column(name = "nome_fabricante", nullable = false, length = 100)
    private String nomeFabricante;

    @Column(name = "codigo_produto", unique = true, length = 50)
    private String codigoProduto;

    // Preço unitário base. A unidade (kg ou litro) será inferida pela classe filha.
    @Column(name = "preco_unitario_base", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUnitarioBase;

    // Controle de inativação lógica (Soft Delete). 
    // Nunca apagamos um consumível do banco para não corromper histórico de Machine Learning.
    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;
}