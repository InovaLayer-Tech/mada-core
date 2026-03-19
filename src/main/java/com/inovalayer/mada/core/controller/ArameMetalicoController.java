package com.inovalayer.mada.core.controller;

import com.inovalayer.mada.core.dto.ArameMetalicoResponseDTO;
import com.inovalayer.mada.core.service.ArameMetalicoService;
import com.inovalayer.mada.core.domain.ArameMetalico;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import java.util.List;

/**
 * REST Controller para o catálogo de arames metálicos.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/arames")
@RequiredArgsConstructor
public class ArameMetalicoController {

    private final ArameMetalicoService arameMetalicoService;

    @GetMapping
    public ResponseEntity<List<ArameMetalicoResponseDTO>> listarCatalogo() {
        return ResponseEntity.ok(arameMetalicoService.listarTodos());
    }

    @PostMapping
    public ResponseEntity<ArameMetalicoResponseDTO> criar(@RequestBody ArameMetalico arame) {
        log.info("RECEBIDA REQUISIÇÃO POST PARA NOVO ARAME: {}", arame.getNome());
        return ResponseEntity.ok(arameMetalicoService.salvar(arame));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArameMetalicoResponseDTO> atualizar(@PathVariable UUID id, @RequestBody ArameMetalico arame) {
        log.info("RECEBIDA REQUISIÇÃO PUT PARA ATUALIZAR ARAME ID {}: {}", id, arame.getNome());
        arame.setId(id);
        return ResponseEntity.ok(arameMetalicoService.salvar(arame));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        log.info("RECEBIDA REQUISIÇÃO DELETE PARA ARAME ID {}", id);
        arameMetalicoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ArameMetalicoResponseDTO> alterarStatus(@PathVariable UUID id) {
        log.info("RECEBIDA REQUISIÇÃO PATCH PARA ALTERAR STATUS DO ARAME ID {}", id);
        return ResponseEntity.ok(arameMetalicoService.alterarStatus(id));
    }
}