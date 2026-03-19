package com.inovalayer.mada.core.service;

import com.inovalayer.mada.core.dto.ArameMetalicoResponseDTO;
import com.inovalayer.mada.core.mapper.ArameMetalicoMapper;
import com.inovalayer.mada.core.repository.ArameMetalicoRepository;
import com.inovalayer.mada.core.domain.ArameMetalico;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Serviço responsável pela orquestração de dados referentes aos Arames Metálicos.
 * Atua como intermediário entre a camada de controle (API) e acesso a dados (Repository).
 */
@Slf4j
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
     * @return Lista contendo os dados de transferência (ArameMetalicoResponseDTO)
     */
    @Transactional(readOnly = true)
    public List<ArameMetalicoResponseDTO> listarTodos() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Transactional
    public ArameMetalicoResponseDTO salvar(ArameMetalico arame) {
        log.info("SALVANDO ARAME METÁLICO: {} (Fornecedor: {})", arame.getNome(), arame.getFornecedor());
        ArameMetalico saved = repository.save(arame);
        return mapper.toDto(saved);
    }

    @Transactional
    public void excluir(UUID id) {
        log.info("EXCLUINDO ARAME METÁLICO ID: {}", id);
        repository.deleteById(id);
    }

    @Transactional
    public ArameMetalicoResponseDTO alterarStatus(UUID id) {
        ArameMetalico arame = repository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Arame não encontrado"));
        
        boolean novoStatus = !arame.getAtivo();
        log.info("ALTERANDO STATUS DO ARAME {} PARA: {}", arame.getNome(), novoStatus ? "ATIVO" : "INATIVO");
        
        arame.setAtivo(novoStatus);
        return mapper.toDto(repository.save(arame));
    }
}