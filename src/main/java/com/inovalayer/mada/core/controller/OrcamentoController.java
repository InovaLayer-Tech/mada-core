package com.inovalayer.mada.core.controller;

import com.inovalayer.mada.core.dto.OrcamentoCalculoRequestDTO;
import com.inovalayer.mada.core.dto.OrcamentoRequestDTO;
import com.inovalayer.mada.core.dto.OrcamentoResponseDTO;
import com.inovalayer.mada.core.service.OrcamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller para expor os serviços de orçamentos WAAM.
 */
@Slf4j
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
        log.info("RECEBIDA SOLICITAÇÃO B2C DE ORÇAMENTO PARA PROJETO: {}", request.nomeProjeto());
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
        log.info("RECEBIDA SOLICITAÇÃO B2B DE CÁLCULO PARA ORÇAMENTO ID: {}", id);
        OrcamentoResponseDTO orcamentoCalculado = orcamentoService.processarCalculo(id, request);
        return ResponseEntity.ok(orcamentoCalculado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrcamentoResponseDTO> buscarOrcamento(@PathVariable UUID id) {
        log.info("CONSULTA DE ORÇAMENTO ID: {}", id);
        OrcamentoResponseDTO orcamento = orcamentoService.buscarPorId(id);
        return ResponseEntity.ok(orcamento);
    }

    @GetMapping
    public ResponseEntity<List<OrcamentoResponseDTO>> listarTodos() {
        log.info("LISTAGEM GERAL DE ORÇAMENTOS");
        return ResponseEntity.ok(orcamentoService.listarTodos());
    }

    @PatchMapping("/{id}/aprovar")
    public ResponseEntity<OrcamentoResponseDTO> aprovar(@PathVariable UUID id) {
        log.info("RECEBIDA REQUISIÇÃO DE APROVAÇÃO PARA ORÇAMENTO ID: {}", id);
        return ResponseEntity.ok(orcamentoService.aprovarOrcamento(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        log.info("RECEBIDA REQUISIÇÃO DE EXCLUSÃO PARA ORÇAMENTO ID: {}", id);
        orcamentoService.excluirOrcamento(id);
        return ResponseEntity.noContent().build();
    }
}