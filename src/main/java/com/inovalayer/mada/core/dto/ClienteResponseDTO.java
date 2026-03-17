package com.inovalayer.mada.core.dto;

import java.util.UUID;

public record ClienteResponseDTO(
    UUID id,
    String nomeRazaoSocial,
    String cnpj,
    String setorAtuacao,
    boolean ativo,
    boolean vip
) {}
