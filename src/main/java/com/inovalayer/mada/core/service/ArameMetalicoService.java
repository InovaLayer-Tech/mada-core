package com.inovalayer.mada.core.service;

import com.inovalayer.mada.core.dto.ArameMetalicoResponseDTO;
import com.inovalayer.mada.core.mapper.ArameMetalicoMapper;
import com.inovalayer.mada.core.repository.ArameMetalicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço responsável pelas regras de negócio e orquestração de Arames Metálicos.
 */
@Service
@RequiredArgsConstructor
public class ArameMetalicoService {

    private final ArameMetalicoRepository arameMetalicoRepository;
    private final ArameMetalicoMapper arameMetalicoMapper;

    /**
     * Recupera todos os arames ativos e os converte em DTOs imutáveis.
     * A anotação readOnly = true otimiza a conexão com o banco, desativando o dirty checking do Hibernate.
     */
    @Transactional(readOnly = true)
    public List<ArameMetalicoResponseDTO> listarTodosAtivos() {
        return arameMetalicoRepository.findByAtivoTrue()
                .stream()
                .map(arameMetalicoMapper::toDto)
                .collect(Collectors.toList());
    }
}