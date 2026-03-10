package com.inovalayer.mada.core.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Criei esta entidade para representar o insumo físico na metrologia.
 * Ela herda da classe abstrata Consumivel, garantindo que o banco de dados
 * faça o JOIN automático entre os dados físicos (desta tabela) e os comerciais (da tabela pai).
 */
@Entity
@Table(name = "tb_arame_metalico")
@PrimaryKeyJoinColumn(name = "id")
@Getter
@Setter
public class ArameMetalico extends Consumivel {

    // Especifiquei o tamanho da coluna no banco para otimizar o armazenamento.
    @Column(name = "liga_metalica", nullable = false, length = 100)
    private String ligaMetalica;

    // Utilizo Double para grandezas físicas contínuas, mantendo o rigor exigido nos cálculos de engenharia.
    @Column(name = "diametro_mm", nullable = false)
    private Double diametroMm;

    @Column(name = "densidade_g_cm3", nullable = false)
    private Double densidadeGcm3;
}