package com.inovalayer.mada.core.controller;

import com.inovalayer.mada.core.domain.Orcamento;
import com.inovalayer.mada.core.dto.OrcamentoRequestDTO;
import com.inovalayer.mada.core.service.OrcamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Ponto de entrada (Entrypoint) da API de Orçamentos.
 * Recebe chamadas HTTP, delega o processamento para o Service e devolve respostas padronizadas em JSON.
 */
@RestController
@RequestMapping("/api/v1/orcamentos")
@RequiredArgsConstructor
public class OrcamentoController {

    private final OrcamentoService orcamentoService;

    /**
     * Endpoint para gerar um novo orçamento.
     * Mapeado para o verbo HTTP POST.
     * * @param request O DTO validado recebido no corpo da requisição (JSON).
     * @return O orçamento gerado e o status HTTP 201 (Created).
     */
    @PostMapping
    public ResponseEntity<Orcamento> criarOrcamento(@Valid @RequestBody OrcamentoRequestDTO request) {
        
        // Extrai os dados validados do DTO (a "prancheta") e repassa ao Service
        Orcamento orcamentoGerado = orcamentoService.gerarOrcamento(
                request.clienteId(),
                request.arameId(),
                request.tempoArcoMinutos(),
                request.massaEstimadaKg()
        );

        // Retorna HTTP 201 indicando sucesso na criação do recurso
        return ResponseEntity.status(HttpStatus.CREATED).body(orcamentoGerado);
    }
}