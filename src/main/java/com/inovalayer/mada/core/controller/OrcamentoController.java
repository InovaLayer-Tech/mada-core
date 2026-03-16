package com.inovalayer.mada.core.controller;

import com.inovalayer.mada.core.domain.Orcamento;
import com.inovalayer.mada.core.dto.OrcamentoRequestDTO;
import com.inovalayer.mada.core.dto.OrcamentoResponseDTO;
import com.inovalayer.mada.core.service.OrcamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

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
    public ResponseEntity<OrcamentoResponseDTO> processarOrcamento(@RequestBody @Valid OrcamentoRequestDTO request) {
        OrcamentoResponseDTO orcamentoGerado = orcamentoService.criarOrcamento(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(orcamentoGerado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrcamentoResponseDTO> buscarOrcamento(@PathVariable UUID id) {
        OrcamentoResponseDTO orcamento = orcamentoService.buscarPorId(id);
        return ResponseEntity.ok(orcamento);
    }
}