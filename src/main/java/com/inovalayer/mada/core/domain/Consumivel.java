package com.inovalayer.mada.core.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Criei esta classe base abstrata para centralizar os dados comerciais (nome, fabricante, preço).
 * Utilizo a estratégia JOINED para que o banco de dados normalize essas informações em uma tabela 'pai',
 * garantindo o princípio DRY (Don't Repeat Yourself) para qualquer novo insumo que eu criar no futuro.
 */
@Entity
@Table(name = "tb_consumivel")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class Consumivel {

    // Utilizei a anotação Include para garantir que o equals() do Java compare os objetos apenas pelo ID,
    // evitando bugs de performance quando eu trabalhar com listas ou coleções no Hibernate.
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 100)
    private String fabricante;

    // Apliquei a restrição unique=true para blindar o banco contra a duplicidade de SKUs/Códigos de produto.
    @Column(name = "codigo_produto", nullable = false, unique = true, length = 50)
    private String codigoProduto;

    // Defini a precisão máxima de 12 dígitos com 6 casas decimais rigorosas.
    // Isso é mandatório para que o cálculo financeiro do Spring Boot não divirja do motor metrológico em Python.
    @Column(name = "preco_unitario_base", nullable = false, precision = 12, scale = 6)
    private BigDecimal precoUnitarioBase;
}