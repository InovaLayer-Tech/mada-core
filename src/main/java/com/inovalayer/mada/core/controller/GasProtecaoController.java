package com.inovalayer.mada.core.controller;

import com.inovalayer.mada.core.dto.GasProtecaoResponseDTO;
import com.inovalayer.mada.core.service.GasProtecaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller para exposição dos endpoints de Gases de Proteção.
 */
@RestController
@RequestMapping("/api/v1/gases")
@RequiredArgsConstructor
public class GasProtecaoController {

    private final GasProtecaoService service;

    @GetMapping
    public ResponseEntity<List<GasProtecaoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }
}
