package com.inovalayer.mada.core.controller;

import com.inovalayer.mada.core.dto.ArameMetalicoResponseDTO;
import com.inovalayer.mada.core.service.ArameMetalicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Endpoint responsável por expor o catálogo de arames metálicos para a interface web.
 */
@RestController
@RequestMapping("/api/v1/arames")
@RequiredArgsConstructor
public class ArameMetalicoController {

    private final ArameMetalicoService arameMetalicoService;

    /**
     * Recupera a lista de todos os arames ativos no sistema, mapeados de forma segura via DTO.
     * @return HTTP 200 (OK) com a lista de arames.
     */
    @GetMapping
    public ResponseEntity<List<ArameMetalicoResponseDTO>> listarAramesDisponiveis() {
        List<ArameMetalicoResponseDTO> arames = arameMetalicoService.listarTodosAtivos();
        return ResponseEntity.ok(arames);
    }
}