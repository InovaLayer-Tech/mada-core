package com.inovalayer.mada.core.service;

import com.inovalayer.mada.core.domain.Cliente;
import com.inovalayer.mada.core.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Camada de Serviço para a entidade Cliente.
 * Concentra as regras de negócio antes de interagir com o PostgreSQL.
 */
@Service
@RequiredArgsConstructor // Gera um construtor com os campos 'final', injetando as dependências automaticamente
public class ClienteService {

    private final ClienteRepository clienteRepository;

    /**
     * @Transactional(readOnly = true) otimiza a performance no Hibernate 
     * pois ele não precisa criar um "snapshot" em memória para verificar atualizações.
     */
    @Transactional(readOnly = true)
    public List<Cliente> buscarTodos() {
        return clienteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorId(UUID id) {
        // Se não encontrar, lança uma exceção. No futuro, criaremos um manipulador global para devolver erro 404.
        return clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado com o ID: " + id));
    }

    /**
     * REGRA DE NEGÓCIO: Não podemos ter dois clientes com o mesmo CNPJ.
     * @Transactional sem readOnly indica que faremos alterações no banco de dados.
     */
    @Transactional
    public Cliente salvar(Cliente cliente) {
        // Validação da regra de negócio
        if (clienteRepository.findByCnpj(cliente.getCnpj()).isPresent()) {
            throw new IllegalArgumentException("Já existe um cliente cadastrado com este CNPJ.");
        }
        
        return clienteRepository.save(cliente);
    }
}