package com.inovalayer.mada.core.service;

import com.inovalayer.mada.core.dto.GasProtecaoResponseDTO;
import com.inovalayer.mada.core.mapper.GasProtecaoMapper;
import com.inovalayer.mada.core.repository.GasProtecaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Serviço responsável por gerenciar os Gases de Proteção.
 */
@Service
public class GasProtecaoService {

    private final GasProtecaoRepository repository;
    private final GasProtecaoMapper mapper;

    public GasProtecaoService(GasProtecaoRepository repository, GasProtecaoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<GasProtecaoResponseDTO> listarTodos() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }
}
