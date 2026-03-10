package com.inovalayer.mada.core.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * O Relações Públicas (Porta-voz) da nossa API.
 * Intercepta exceções lançadas em qualquer parte do código e traduz para respostas HTTP amigáveis.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Intercepta a IllegalArgumentException.
     * Nós utilizámos esta exceção no OrcamentoService quando uma regra de negócio é violada 
     * (ex: Cliente não encontrado, Arame não encontrado).
     * Mapeamos para HTTP 400 (Bad Request), pois o erro partiu de um dado inválido enviado pelo utilizador.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<StandardErrorDTO> handleIllegalArgumentException(
            IllegalArgumentException ex, 
            HttpServletRequest request) {

        StandardErrorDTO error = new StandardErrorDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Erro de Validação de Negócio",
                ex.getMessage(), // Mensagem limpa: "Cliente não encontrado com o ID fornecido."
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Intercepta a MethodArgumentNotValidException.
     * Esta exceção é lançada pelo Spring quando as validações do nosso DTO (como o @NotNull ou @Positive) falham.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardErrorDTO> handleValidationExceptions(
            MethodArgumentNotValidException ex, 
            HttpServletRequest request) {

        // Extrai todas as mensagens de erro dos campos que falharam na validação e junta-as numa String
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        StandardErrorDTO error = new StandardErrorDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Falha na Estrutura dos Dados (DTO)",
                errors,
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * O "Cesto de Lixo" Global (Fallback).
     * Se ocorrer um erro catastrófico que não previmos (ex: a base de dados cai), 
     * cai aqui. Mapeamos para 500, mas sem expor o código fonte.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardErrorDTO> handleGenericException(
            Exception ex, 
            HttpServletRequest request) {

        StandardErrorDTO error = new StandardErrorDTO(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro Interno do Servidor",
                "Ocorreu um erro inesperado. Por favor, contacte o suporte da InovaLayer.", // Mensagem genérica e segura
                request.getRequestURI()
        );

        // Opcional: Aqui poderíamos enviar o 'ex.getMessage()' para um sistema de logs (ex: Datadog)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}