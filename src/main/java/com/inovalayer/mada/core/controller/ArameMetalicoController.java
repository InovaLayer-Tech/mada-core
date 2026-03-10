package com.inovalayer.mada.core.controller;

import com.inovalayer.mada.core.dto.ArameMetalicoResponseDTO;
import com.inovalayer.mada.core.service.ArameMetalicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Criei este REST Controller para expor o catálogo de arames metálicos.
 * Ele responde exclusivamente a requisições GET e entrega a lista de DTOs
 * pronta para ser consumida pela interface do usuário.
 */
@RestController
@RequestMapping("/api/v1/arames")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200") // Política de segurança para permitir requisições do Angular
public class ArameMetalicoController {

    private final ArameMetalicoService arameMetalicoService;

    @GetMapping
    public ResponseEntity<List<ArameMetalicoResponseDTO>> listarCatalogo() {
        
        // Delega a busca para o Serviço, que já retorna a lista convertida em DTOs.
        List<ArameMetalicoResponseDTO> catalogo = arameMetalicoService.listarTodos();
        
        // Retorna o HTTP Status 200 (OK) com a lista no corpo (body) da resposta JSON.
        return ResponseEntity.ok(catalogo);
    }
}