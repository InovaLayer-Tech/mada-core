package com.inovalayer.mada.core.controller;

import com.inovalayer.mada.core.dto.GasProtecaoResponseDTO;
import com.inovalayer.mada.core.service.GasProtecaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import com.inovalayer.mada.core.domain.GasProtecao;

import java.util.List;

/**
 * Controller para exposição dos endpoints de Gases de Proteção.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/gases")
@RequiredArgsConstructor
public class GasProtecaoController {

    private final GasProtecaoService service;

    @GetMapping
    public ResponseEntity<List<GasProtecaoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @PostMapping
    public ResponseEntity<GasProtecaoResponseDTO> criar(@RequestBody GasProtecao gas) {
        log.info("RECEBIDA REQUISIÇÃO POST PARA NOVO GÁS: {}", gas.getNome());
        return ResponseEntity.ok(service.salvar(gas));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GasProtecaoResponseDTO> atualizar(@PathVariable UUID id, @RequestBody GasProtecao gas) {
        log.info("RECEBIDA REQUISIÇÃO PUT PARA ATUALIZAR GÁS ID {}: {}", id, gas.getNome());
        gas.setId(id);
        return ResponseEntity.ok(service.salvar(gas));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        log.info("RECEBIDA REQUISIÇÃO DELETE PARA GÁS ID {}", id);
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<GasProtecaoResponseDTO> alterarStatus(@PathVariable UUID id) {
        log.info("RECEBIDA REQUISIÇÃO PATCH PARA ALTERAR STATUS DO GÁS ID {}", id);
        return ResponseEntity.ok(service.alterarStatus(id));
    }
}
