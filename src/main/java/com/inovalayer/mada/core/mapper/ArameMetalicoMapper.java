package com.inovalayer.mada.core.mapper;

import com.inovalayer.mada.core.domain.ArameMetalico;
import com.inovalayer.mada.core.dto.ArameMetalicoResponseDTO;
import org.mapstruct.Mapper;

/**
 * Interface de mapeamento estrutural em tempo de compilação.
 */
@Mapper(componentModel = "spring")
public interface ArameMetalicoMapper {

    /**
     * Converte a entidade de banco de dados para o contrato de rede (DTO).
     * @param entity A entidade isolada do cofre de dados.
     * @return O DTO imutável (record) seguro para tráfego na interface web.
     */
    ArameMetalicoResponseDTO toDto(ArameMetalico entity);
}