package com.inovalayer.mada.core.repository;

import com.inovalayer.mada.core.domain.Orcamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrcamentoRepository extends JpaRepository<Orcamento, UUID> {

    // Como discutimos na mentoria, o relacionamento bidirecional é perigoso.
    // Para listar os orçamentos de um cliente, usamos este método no Repositório do Orçamento,
    // garantindo performance e evitando o problema N+1.
    List<Orcamento> findByClienteId(UUID clienteId);
}