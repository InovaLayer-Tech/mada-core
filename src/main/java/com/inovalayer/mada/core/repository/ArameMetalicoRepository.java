package com.inovalayer.mada.core.repository;

import com.inovalayer.mada.core.domain.ArameMetalico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ArameMetalicoRepository extends JpaRepository<ArameMetalico, UUID> {
    // Caso precise listar apenas ativos no futuro, teremos que adicionar a variável 'ativo' 
    // novamente na classe Consumivel.java e recriar o método aqui.
}