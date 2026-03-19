package com.inovalayer.mada.core.dto;

import com.inovalayer.mada.core.domain.UsuarioRole;
import java.util.UUID;

public record UsuarioResponseDTO(
    UUID id,
    String email,
    String nomeCompleto,
    UsuarioRole role
) {}
