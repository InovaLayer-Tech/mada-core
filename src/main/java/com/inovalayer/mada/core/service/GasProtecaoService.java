package com.inovalayer.mada.core.service;

import com.inovalayer.mada.core.dto.GasProtecaoResponseDTO;
import com.inovalayer.mada.core.mapper.GasProtecaoMapper;
import com.inovalayer.mada.core.repository.GasProtecaoRepository;
import com.inovalayer.mada.core.domain.GasProtecao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Serviço responsável por gerenciar os Gases de Proteção.
 */
@Slf4j
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

    @Transactional
    public GasProtecaoResponseDTO salvar(GasProtecao gas) {
        log.info("SALVANDO GÁS DE PROTEÇÃO: {} (Fornecedor: {})", gas.getNome(), gas.getFornecedor());
        return mapper.toDto(repository.save(gas));
    }

    @Transactional
    public void excluir(java.util.UUID id) {
        log.info("EXCLUINDO GÁS DE PROTEÇÃO ID: {}", id);
        repository.deleteById(id);
    }

    @Transactional
    public GasProtecaoResponseDTO alterarStatus(java.util.UUID id) {
        GasProtecao gas = repository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Gás não encontrado"));
        
        boolean novoStatus = !gas.getAtivo();
        log.info("ALTERANDO STATUS DO GÁS {} PARA: {}", gas.getNome(), novoStatus ? "ATIVO" : "INATIVO");
        
        gas.setAtivo(novoStatus);
        return mapper.toDto(repository.save(gas));
    }
}
