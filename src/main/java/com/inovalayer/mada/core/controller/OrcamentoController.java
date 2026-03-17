package com.inovalayer.mada.core.controller;

import com.inovalayer.mada.core.dto.OrcamentoCalculoRequestDTO;
import com.inovalayer.mada.core.dto.OrcamentoRequestDTO;
import com.inovalayer.mada.core.dto.OrcamentoResponseDTO;
import com.inovalayer.mada.core.service.OrcamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller para expor os serviços de orçamentos WAAM.
 */
@RestController
@RequestMapping("/api/v1/orcamentos")
@RequiredArgsConstructor
public class OrcamentoController {

    private final OrcamentoService orcamentoService;

    /**
     * Registro inicial da intenção de orçamento pelo Cliente.
     */
    @PostMapping
    public ResponseEntity<OrcamentoResponseDTO> solicitarOrcamento(@RequestBody @Valid OrcamentoRequestDTO request) {
        OrcamentoResponseDTO orcamento = orcamentoService.solicitarOrcamento(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(orcamento);
    }

    /**
     * Processamento metrológico e cálculo financeiro pela Engenharia (B2B).
     */
    @PutMapping("/{id}/calcular")
    public ResponseEntity<OrcamentoResponseDTO> processarCalculo(
            @PathVariable UUID id, 
            @RequestBody @Valid OrcamentoCalculoRequestDTO request) {
        OrcamentoResponseDTO orcamentoCalculado = orcamentoService.processarCalculo(id, request);
        return ResponseEntity.ok(orcamentoCalculado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrcamentoResponseDTO> buscarOrcamento(@PathVariable UUID id) {
        OrcamentoResponseDTO orcamento = orcamentoService.buscarPorId(id);
        return ResponseEntity.ok(orcamento);
    }

    @GetMapping
    public ResponseEntity<List<OrcamentoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(orcamentoService.listarTodos());
    }
}