package com.inovalayer.mada.core.controller;

import com.inovalayer.mada.core.dto.ClienteResponseDTO;
import com.inovalayer.mada.core.mapper.ClienteMapper;
import com.inovalayer.mada.core.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService service;
    private final ClienteMapper mapper;

    @GetMapping("/atual")
    public ResponseEntity<ClienteResponseDTO> obterPerfilAtual() {
        // Simulação do cliente atual (pega o primeiro cadastrado no seed)
        return ResponseEntity.ok(
            service.buscarTodos().stream()
                .findFirst()
                .map(mapper::toDto)
                .orElseThrow(() -> new RuntimeException("Nenhum cliente cadastrado no sistema."))
        );
    }
}
