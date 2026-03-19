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
        if (cliente.getCnpj() != null && clienteRepository.findByCnpj(cliente.getCnpj()).isPresent()) {
            throw new IllegalArgumentException("Já existe um cliente cadastrado com este CNPJ.");
        }
        
        return clienteRepository.save(cliente);
    }

    private final com.inovalayer.mada.core.repository.UsuarioRepository usuarioRepository;

    @Transactional
    public Cliente atualizar(UUID id, com.inovalayer.mada.core.dto.ClienteUpdateDTO dados) {
        Cliente cliente = buscarPorId(id);
        
        if (dados.razaoSocial() != null) cliente.setRazaoSocial(dados.razaoSocial());
        if (dados.setorAtuacao() != null) cliente.setSetorAtuacao(dados.setorAtuacao());
        if (dados.cnpj() != null) cliente.setCnpj(dados.cnpj());
        
        // Atualizar informações do Usuário vinculado se fornecidas
        if (cliente.getUsuario() != null) {
            if (dados.nomeCompleto() != null) cliente.getUsuario().setNomeCompleto(dados.nomeCompleto());
            if (dados.email() != null && !cliente.getUsuario().getEmail().equals(dados.email())) {
                // REGRA: E-mail deve ser único se for alterado
                if (usuarioRepository.findByEmail(dados.email()).isPresent()) {
                    throw new IllegalArgumentException("Este e-mail já está em uso por outro usuário.");
                }
                cliente.getUsuario().setEmail(dados.email());
            }
        }
        
        return clienteRepository.save(cliente);
    }
}