package com.inovalayer.mada.core.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.UUID;

/**
 * Projetei este DTO (record) para atuar como a barreira de validação primária (Fail-Fast).
 * Ele intercepta o JSON vindo do Front-end e garante que os dados obedeçam às leis
 * da física e da lógica antes de passarem para a camada de serviço e banco de dados.
 */
public record OrcamentoRequestDTO(
        
        // A anotação @NotNull bloqueia requisições onde o Angular esqueceu de enviar o ID.
        @NotNull(message = "O identificador do insumo (Arame) é obrigatório.")
        UUID arameId,

        // A anotação @Positive impede matematicamente que o usuário ou um hacker
        // envie um "tempo de arco" negativo (ex: -10 minutos), o que corromperia o custo.
        @NotNull(message = "O tempo de arco aberto é obrigatório.")
        @Positive(message = "O tempo de arco deve ser maior que zero.")
        Double tempoArcoMinutos,

        @NotNull(message = "A massa estimada é obrigatória.")
        @Positive(message = "A massa estimada deve ser maior que zero.")
        Double massaEstimadaKg
) {}