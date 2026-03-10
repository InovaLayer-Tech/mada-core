package com.inovalayer.mada.core.repository;

import com.inovalayer.mada.core.domain.ArameMetalico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ArameMetalicoRepository extends JpaRepository<ArameMetalico, UUID> {
    // Método útil para listar apenas os arames que não foram apagados (Soft Delete)
    List<ArameMetalico> findByAtivoTrue();
}