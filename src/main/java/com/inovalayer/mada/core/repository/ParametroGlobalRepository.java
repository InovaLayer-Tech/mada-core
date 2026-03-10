package com.inovalayer.mada.core.repository;

import com.inovalayer.mada.core.domain.ParametroGlobal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ParametroGlobalRepository extends JpaRepository<ParametroGlobal, UUID> {
    // Para esta entidade, precisaremos apenas de encontrar o "primeiro" registo ativo, 
    // ou seja, a configuração global atual. Os métodos padrão do JpaRepository chegam.
}