package com.inovalayer.mada.core.repository;

import com.inovalayer.mada.core.domain.GasProtecao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GasProtecaoRepository extends JpaRepository<GasProtecao, UUID> {
    List<GasProtecao> findByAtivoTrue();
}