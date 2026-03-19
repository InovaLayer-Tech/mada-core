package com.inovalayer.mada.core.mapper;

import com.inovalayer.mada.core.domain.GasProtecao;
import com.inovalayer.mada.core.dto.GasProtecaoResponseDTO;
import org.springframework.stereotype.Component;

/**
 * Mapper para converter a entidade GasProtecao para GasProtecaoResponseDTO.
 */
@Component
public class GasProtecaoMapper {

    public GasProtecaoResponseDTO toDto(GasProtecao entity) {
        if (entity == null) {
            return null;
        }

        return new GasProtecaoResponseDTO(
                entity.getId(),
                entity.getNome(),
                entity.getFornecedor(),
                entity.getCodigoProduto(),
                entity.getTipoGas(),
                entity.getPrecoUnitarioBase(),
                entity.getAtivo(),
                entity.getVazaoPadrao()
        );
    }
}
