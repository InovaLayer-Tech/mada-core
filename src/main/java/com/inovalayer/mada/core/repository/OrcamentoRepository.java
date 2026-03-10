package com.inovalayer.mada.core.repository;

import com.inovalayer.mada.core.domain.Orcamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

/**
 * Interface de persistência para os Orçamentos.
 */
@Repository
public interface OrcamentoRepository extends JpaRepository<Orcamento, UUID> {
    // Centraliza as operações de escrita e leitura dos documentos gerados.
}