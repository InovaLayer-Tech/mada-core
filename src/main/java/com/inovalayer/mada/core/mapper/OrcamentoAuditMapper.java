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
                
                // Dados B2C
                entity.getNomeProjeto(),
                entity.getNomeEmpresa(),
                entity.getFinalidadePeca(),
                entity.getArquivoUrl(),
                entity.getMaterialDesejadoId(),
                entity.getQuantidade(),
                
                entity.getDimensaoX(),
                entity.getDimensaoY(),
                entity.getDimensaoZ(),
                entity.getTolerancia(),
                entity.getAcabamento(),
                entity.getNivelInspecao(),
                entity.getTratamentoTermico(),
                
                // FASE 1: IC
                entity.getFase1IC() != null ? entity.getFase1IC().getTempoPreparacaoMinutos() : 0.0,
                entity.getFase1IC() != null ? entity.getFase1IC().getTempoRemocaoMinutos() : 0.0,
                entity.getFase1IC() != null ? entity.getFase1IC().getCustoSubstrato() : BigDecimal.ZERO,
                entity.getFase1IC() != null ? entity.getFase1IC().getCustoPreparacao() : BigDecimal.ZERO,
                entity.getFase1IC() != null ? entity.getFase1IC().getCustoRemocao() : BigDecimal.ZERO,
                entity.getFase1IC() != null ? entity.getFase1IC().getCustoEngenharia() : BigDecimal.ZERO,
                entity.getFase1IC() != null ? entity.getFase1IC().getCustoTotalIC() : BigDecimal.ZERO,
                
                // FASE 2: DC
                entity.getFase2DC() != null ? entity.getFase2DC().getTempoArcoMinutos() : 0.0,
                entity.getFase2DC() != null ? entity.getFase2DC().getTempoMortoMinutos() : 0.0,
                entity.getFase2DC() != null ? entity.getFase2DC().getMassaEstimadaKg() : 0.0,
                (entity.getFase2DC() != null && entity.getFase2DC().getArameMetalico() != null) 
                        ? (entity.getFase2DC().getArameMetalico().getLigaMetalica() + " " + entity.getFase2DC().getArameMetalico().getCodigoProduto()) 
                        : "N/A",
                entity.getFase2DC() != null ? entity.getFase2DC().getCustoMaterial() : BigDecimal.ZERO,
                entity.getFase2DC() != null ? entity.getFase2DC().getCustoGas() : BigDecimal.ZERO,
                entity.getFase2DC() != null ? entity.getFase2DC().getCustoEnergia() : BigDecimal.ZERO,
                entity.getFase2DC() != null ? entity.getFase2DC().getCustoMaquina() : BigDecimal.ZERO,
                entity.getFase2DC() != null ? entity.getFase2DC().getCustoTotalDC() : BigDecimal.ZERO,
                
                servicosDto,
                totalAC,
                
                custoDireto,
                margem,
                impostos,
                precoFinal
        );
    }
}
