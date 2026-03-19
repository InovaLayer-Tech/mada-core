package com.inovalayer.mada.core.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SenhaUpdateDTO(
    @NotBlank(message = "A senha atual é obrigatória")
    String senhaAntiga,

    @NotBlank(message = "A nova senha é obrigatória")
    @Size(min = 8, message = "A nova senha deve ter no mínimo 8 caracteres")
    String novaSenha,

    @NotBlank(message = "A confirmação da nova senha é obrigatória")
    String confirmacaoSenha
) {}
