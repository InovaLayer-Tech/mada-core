package com.inovalayer.mada.core.controller;

import com.inovalayer.mada.core.dto.ClienteResponseDTO;
import com.inovalayer.mada.core.mapper.ClienteMapper;
import com.inovalayer.mada.core.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService service;
    private final ClienteMapper mapper;

    @GetMapping("/atual")
    public ResponseEntity<ClienteResponseDTO> obterPerfilAtual() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        // Buscar cliente vinculado ao usuário autenticado
        return ResponseEntity.ok(
            service.buscarTodos().stream()
                .filter(c -> c.getUsuario() != null && c.getUsuario().getEmail().equals(email))
                .findFirst()
                .map(mapper::toDto)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado para o usuário logado."))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizar(@PathVariable java.util.UUID id, @org.springframework.web.bind.annotation.RequestBody com.inovalayer.mada.core.dto.ClienteUpdateDTO dados) {
        return ResponseEntity.ok(mapper.toDto(service.atualizar(id, dados)));
    }
}
