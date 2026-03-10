package com.inovalayer.mada.core.repository;

import com.inovalayer.mada.core.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Interface responsável pela persistência e consulta da entidade Cliente.
 * A anotação @Repository é opcional quando estendemos JpaRepository, mas é uma boa prática 
 * para clareza e para o Spring converter exceções de SQL em exceções de dados legíveis (DataAccessException).
 */
@Repository
// JpaRepository requer dois parâmetros genéricos: <A Classe da Entidade, O Tipo da Chave Primária>
public interface ClienteRepository extends JpaRepository<Cliente, UUID> {

    // Derived Query Method: O Spring cria o SQL automaticamente lendo o nome do método.
    // Usamos Optional porque o cliente pode ou não existir com esse CNPJ.
    Optional<Cliente> findByCnpj(String cnpj);
}