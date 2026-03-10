package com.inovalayer.mada.core.mapper;

import com.inovalayer.mada.core.domain.ArameMetalico;
import com.inovalayer.mada.core.dto.ArameMetalicoResponseDTO;
import org.springframework.stereotype.Component;

/**
 * Criei este Mapper como um Componente gerenciado pelo Spring (Injeção de Dependência).
 * Ele centraliza a responsabilidade única (SRP do SOLID) de converter a Entidade física
 * do banco de dados no DTO de transporte que o Angular espera, achatando a herança.
 */
@Component
public class ArameMetalicoMapper {

    public ArameMetalicoResponseDTO toResponseDTO(ArameMetalico entity) {
        if (entity == null) {
            return null;
        }

        // Mapeio explicitamente o 'fabricante' (da classe pai Consumivel) 
        // para o 'nomeFabricante' que o contrato do Front-end exige.
        return new ArameMetalicoResponseDTO(
                entity.getId(),
                entity.getFabricante(),
                entity.getCodigoProduto(),
                entity.getPrecoUnitarioBase(),
                entity.getLigaMetalica(),
                entity.getDiametroMm(),
                entity.getDensidadeGcm3()
        );
    }
}