package com.inovalayer.mada.core.exception;

import java.time.LocalDateTime;

/**
 * Record DTO para padronizar o formato (shape) de todas as mensagens de erro da API.
 * Qualquer erro que saia do sistema do MADA terá sempre esta estrutura JSON.
 */
public record StandardErrorDTO(
        LocalDateTime timestamp,  // Momento exato em que o erro ocorreu (para logs e auditoria)
        Integer status,           // O código HTTP do erro (ex: 400, 404, 500)
        String error,             // Um título curto para o erro (ex: "Bad Request")
        String message,           // A mensagem detalhada e amigável para o Front-end ler
        String path               // A rota (URL) que o utilizador tentou aceder e que gerou o erro
) {
}