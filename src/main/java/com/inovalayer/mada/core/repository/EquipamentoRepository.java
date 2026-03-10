package com.inovalayer.mada.core.repository;

import com.inovalayer.mada.core.domain.Equipamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EquipamentoRepository extends JpaRepository<Equipamento, UUID> {
}