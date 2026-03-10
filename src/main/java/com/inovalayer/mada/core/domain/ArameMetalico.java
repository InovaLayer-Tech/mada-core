package com.inovalayer.mada.core.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Entidade filha (Especialização) de Consumível, focada nos parâmetros do metal de adição.
 * Localização: src/main/java/com/inovalayer/mada/core/domain/
 */
@Getter
@Setter
@Entity
@Table(name = "tb_arame_metalico")
// Herda ID e campos básicos de tb_consumivel. O JPA gerencia a inserção em ambas as tabelas automaticamente.
public class ArameMetalico extends Consumivel {

    @Column(name = "liga_metalica", nullable = false, length = 80) // Ex: ER70S-6, Inox 316L
    private String ligaMetalica;

    @Column(name = "diametro_mm", nullable = false, precision = 4, scale = 2) // Ex: 1.20 mm
    private BigDecimal diametroMm;

    // Crucial para conversão: Se o Python devolver o volume da peça (cm³), 
    // multiplicamos por esta densidade para saber quantos KG de arame foram gastos.
    @Column(name = "densidade_g_cm3", nullable = false, precision = 6, scale = 3)
    private BigDecimal densidadeGCm3;
}