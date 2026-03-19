package com.inovalayer.mada.core.dto;

import java.util.UUID;

public record ClienteUpdateDTO(
    String nomeCompleto,
    String email,
    String razaoSocial,
    String setorAtuacao,
    String cnpj
) {}
