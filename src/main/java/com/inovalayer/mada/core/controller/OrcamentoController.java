package com.inovalayer.mada.core.controller;

import com.inovalayer.mada.core.domain.Orcamento;
import com.inovalayer.mada.core.dto.OrcamentoRequestDTO;
import com.inovalayer.mada.core.service.OrcamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Criei este REST Controller para expor os serviços de metrologia para a rede externa.
 * Ele atua como o ponto de entrada (Endpoint) da API, recebendo pacotes JSON do Angular,
 * acionando as validações de segurança e devolvendo o código de status HTTP correto.
 */
@RestController
@RequestMapping("/api/v1/orcamentos")
@RequiredArgsConstructor
public class OrcamentoController {

    private final OrcamentoService orcamentoService;

    // A anotação @Valid é o gatilho que liga a barreira de segurança que criamos no DTO.
    @PostMapping
    public ResponseEntity<Orcamento> processarOrcamento(@RequestBody @Valid OrcamentoRequestDTO request) {
        
        // Delega a complexidade matemática e de banco de dados para a camada de Serviço
        Orcamento orcamentoGerado = orcamentoService.processarMetrologia(request);
        
        // Retorna o HTTP Status 201 (Created), que é o padrão RESTful para quando um recurso nasce no servidor
        return ResponseEntity.status(HttpStatus.CREATED).body(orcamentoGerado);
    }
}