package com.inovalayer.mada.core.controller;

import com.inovalayer.mada.core.domain.ParametroGlobal;
import com.inovalayer.mada.core.service.ParametroGlobalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/configuracoes")
@RequiredArgsConstructor
public class ParametroGlobalController {

    private final ParametroGlobalService service;

    @GetMapping
    public ResponseEntity<ParametroGlobal> obterConfiguracao() {
        return ResponseEntity.ok(service.obterConfiguracao());
    }

    @PutMapping
    public ResponseEntity<ParametroGlobal> atualizar(@RequestBody ParametroGlobal configuracao) {
        return ResponseEntity.ok(service.atualizar(configuracao));
    }
}
