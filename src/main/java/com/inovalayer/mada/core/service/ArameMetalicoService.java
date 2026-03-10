package com.inovalayer.mada.core.service;

import com.inovalayer.mada.core.domain.ArameMetalico;
import com.inovalayer.mada.core.dto.ArameMetalicoResponseDTO;
import com.inovalayer.mada.core.mapper.ArameMetalicoMapper;
import com.inovalayer.mada.core.repository.ArameMetalicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Criei este Service para isolar a lógica de leitura do catálogo de insumos.
 * Ele orquestra a busca na base de dados e a imediata conversão (mapeamento)
 * para o formato DTO, garantindo que a Entidade física nunca vaze para a rede externa.
 */
@Service
@RequiredArgsConstructor
public class ArameMetalicoService {

    private final ArameMetalicoRepository arameMetalicoRepository;
    private final ArameMetalicoMapper arameMetalicoMapper;

    // A anotação readOnly = true otimiza a performance do banco de dados,
    // informando ao Hibernate que não faremos alterações (INSERT/UPDATE), 
    // dispensando o custoso controle de estado na memória (Dirty Checking).
    @Transactional(readOnly = true)
    public List<ArameMetalicoResponseDTO> listarTodos() {
        
        // 1. Busca todas as entidades físicas na base de dados
        List<ArameMetalico> arames = arameMetalicoRepository.findAll();

        // 2. Converte as Entidades em DTOs usando a API de Streams do Java (Programação Funcional)
        return arames.stream()
                .map(arameMetalicoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}