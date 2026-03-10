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
 */
@Getter
@Setter
@Entity
@Table(name = "tb_consumivel")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Consumivel extends BaseEntity {

    @Column(name = "nome_fabricante", nullable = false, length = 100)
    private String nomeFabricante;

    @Column(name = "codigo_produto", unique = true, length = 50)
    private String codigoProduto;

    // CORREÇÃO: Precisão ampliada para rigor metrológico (precision = 19, scale = 6)
    @Column(name = "preco_unitario_base", nullable = false, precision = 19, scale = 6)
    private BigDecimal precoUnitarioBase;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;
}