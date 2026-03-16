package com.inovalayer.mada.core.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_arame_metalico")
@Data
@EqualsAndHashCode(callSuper = true)
public class ArameMetalico extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(name = "fabricante", length = 100)
    private String fabricante;

    @Column(name = "codigo_produto", length = 50)
    private String codigoProduto;

    @Column(name = "liga_metalica", length = 50)
    private String ligaMetalica;

    @Column(name = "diametro_mm")
    private Double diametroMm;

    @Column(name = "tipo_material", length = 50)
    private String tipoMaterial;

    @Column(name = "preco_unitario_base", nullable = false, precision = 12, scale = 2)
    private BigDecimal precoUnitarioBase;

    @Column(nullable = false)
    private Boolean ativo = true;

    // Novos campos essenciais para o cálculo de volume -> massa (Fase DC)
    @Column(name = "densidade_gcm3", nullable = false, precision = 8, scale = 4)
    private Double densidadeGcm3; // Representado em g/cm³

    @Column(nullable = false, precision = 5, scale = 2)
    private Double eficiencia; // Representado em % (ex: 97.5)
}