package com.inovalayer.mada.core.mapper;

import com.inovalayer.mada.core.domain.ArameMetalico;
import com.inovalayer.mada.core.dto.ArameMetalicoResponseDTO;
import org.springframework.stereotype.Component;

/**
 * Componente (Singleton) responsável pelo mapeamento da entidade ArameMetalico para seu respectivo DTO.
 * Centraliza a conversão e protege a camada de Service de lidar com regras de formatação de dados.
 */
@Component
public class ArameMetalicoMapper {

    public ArameMetalicoResponseDTO toDto(ArameMetalico entity) {
        if (entity == null) {
            return null; // Null-safety: previne NullPointerException no Service
        }

        // Instanciação direta do Record. Os atributos 'nome' e 'fabricante'
        // são herdados automaticamente da superclasse Consumivel (Estratégia JOINED).
        return new ArameMetalicoResponseDTO(
                entity.getId(),
                entity.getNome(),
                entity.getFornecedor(),
                entity.getCodigoProduto(),
                entity.getPrecoUnitarioBase(),
                entity.getLigaMetalica(),
                entity.getDiametroMm(),
                entity.getDensidadeGcm3(),
                entity.getEficiencia(),
                entity.getAtivo()
        );
    }
}