package com.inovalayer.mada.core.mapper;

import com.inovalayer.mada.core.domain.Orcamento;
import com.inovalayer.mada.core.domain.OrcamentoAC;
import com.inovalayer.mada.core.dto.OrcamentoResponseDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper manual (ou poderia ser MapStruct) isolando a lógica de conversão da Entidade para ResponseDTO.
 * Aplica lógica de negócio de precificação final (Margem + Impostos).
 */
@Component
public class OrcamentoAuditMapper {

    private static final BigDecimal MARGEM_LUCRO_PERCENTUAL = new BigDecimal("0.30"); // 30% Hardcoded por equanto (Mockup)
    private static final BigDecimal IMPOSTOS_PERCENTUAL = new BigDecimal("0.12"); // 12% aproximado das imagens (Mockup)

    public OrcamentoResponseDTO toAuditDto(Orcamento entity) {
        
        List<OrcamentoResponseDTO.ServicoAdicionalDTO> servicosDto = entity.getFase3AC().stream()
                .map(ac -> new OrcamentoResponseDTO.ServicoAdicionalDTO(
                        ac.getDescricaoServico(),
                        ac.getQuantidadeHoras(),
                        ac.getCustoTotalAC()
                )).collect(Collectors.toList());

        BigDecimal totalAC = entity.getFase3AC().stream()
                .map(OrcamentoAC::getCustoTotalAC)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Precificação
        BigDecimal custoDireto = entity.getCustoTotalFinal();
        BigDecimal margem = custoDireto.multiply(MARGEM_LUCRO_PERCENTUAL).setScale(2, RoundingMode.HALF_UP);
        BigDecimal baseImpostos = custoDireto.add(margem);
        BigDecimal impostos = baseImpostos.multiply(IMPOSTOS_PERCENTUAL).setScale(2, RoundingMode.HALF_UP);
        BigDecimal precoFinal = baseImpostos.add(impostos).setScale(2, RoundingMode.HALF_UP);

        return new OrcamentoResponseDTO(
                entity.getId(),
                entity.getStatus().name(),
                entity.getDataEmissao(),
                
                entity.getFase1IC().getTempoPreparacaoMinutos(),
                entity.getFase1IC().getTempoRemocaoMinutos(),
                entity.getFase1IC().getCustoSubstrato(),
                entity.getFase1IC().getCustoPreparacao(),
                entity.getFase1IC().getCustoRemocao(),
                entity.getFase1IC().getCustoTotalIC(),
                
                entity.getFase2DC().getTempoArcoMinutos(),
                entity.getFase2DC().getMassaEstimadaKg(),
                entity.getFase2DC().getArameMetalico().getLigaMetalica() + " " + entity.getFase2DC().getArameMetalico().getCodigoProduto(),
                entity.getFase2DC().getCustoMaterial(),
                entity.getFase2DC().getCustoGas(),
                entity.getFase2DC().getCustoEnergia(),
                entity.getFase2DC().getCustoMaquina(),
                entity.getFase2DC().getCustoTotalDC(),
                
                servicosDto,
                totalAC,
                
                custoDireto,
                margem,
                impostos,
                precoFinal
        );
    }
}
