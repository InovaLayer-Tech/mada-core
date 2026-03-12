package com.inovalayer.mada.core.service;

import com.inovalayer.mada.core.dto.ArameMetalicoResponseDTO;
import com.inovalayer.mada.core.mapper.ArameMetalicoMapper;
import com.inovalayer.mada.core.repository.ArameMetalicoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Serviço responsável pela orquestração de dados referentes aos Arames Metálicos.
 * Atua como intermediário entre a camada de controle (API) e acesso a dados (Repository).
 */
@Service
public class ArameMetalicoService {

    private final ArameMetalicoRepository repository;
    private final ArameMetalicoMapper mapper;

    // Injeção de dependência via construtor (Padrão Ouro do Spring Boot)
    public ArameMetalicoService(ArameMetalicoRepository repository, ArameMetalicoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Retorna a lista completa de arames metálicos mapeados para DTO.
     * * @return Lista contendo os dados de transferência (ArameMetalicoResponseDTO)
     */
    @Transactional(readOnly = true)
    public List<ArameMetalicoResponseDTO> listarTodos() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList(); // Método nativo do Java 16+ para fechamento de Streams
    }
}