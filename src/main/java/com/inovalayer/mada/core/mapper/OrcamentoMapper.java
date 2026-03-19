package com.inovalayer.mada.core.mapper;

import com.inovalayer.mada.core.domain.Orcamento;
import com.inovalayer.mada.core.domain.OrcamentoAC;
import com.inovalayer.mada.core.dto.OrcamentoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrcamentoMapper {

    @Mapping(target = "custoSubstratoIC", source = "fase1IC.custoSubstrato")
    @Mapping(target = "custoPreparacaoIC", source = "fase1IC.custoPreparacao")
    @Mapping(target = "custoRemocaoIC", source = "fase1IC.custoRemocao")
    @Mapping(target = "custoEngenhariaIC", source = "fase1IC.custoEngenharia")
    @Mapping(target = "custoTotalIC", source = "fase1IC.custoTotalIC")
    @Mapping(target = "tempoPreparacaoMinutos", source = "fase1IC.tempoPreparacaoMinutos")
    @Mapping(target = "tempoRemocaoMinutos", source = "fase1IC.tempoRemocaoMinutos")
    @Mapping(target = "nomeEmpresa", source = "nomeEmpresa")
    
    @Mapping(target = "tempoArcoMinutos", source = "fase2DC.tempoArcoMinutos")
    @Mapping(target = "tempoMortoMinutos", source = "fase2DC.tempoMortoMinutos")
    @Mapping(target = "massaEstimadaKg", source = "fase2DC.massaEstimadaKg")
    @Mapping(target = "nomeArameMetalico", source = "fase2DC.arameMetalico.nome")
    @Mapping(target = "custoMaterialDC", source = "fase2DC.custoMaterial")
    @Mapping(target = "custoGasDC", source = "fase2DC.custoGas")
    @Mapping(target = "custoEnergiaDC", source = "fase2DC.custoEnergia")
    @Mapping(target = "custoMaquinaDC", source = "fase2DC.custoMaquina")
    @Mapping(target = "custoTotalDC", source = "fase2DC.custoTotalDC")
    
    @Mapping(target = "servicosAC", source = "fase3AC")
    @Mapping(target = "custoTotalAC", expression = "java(calcularTotalAC(orcamento.getFase3AC()))")
    
    @Mapping(target = "custoDiretoFabricacao", source = "custoTotalFinal")
    @Mapping(target = "margemComercialAplicada", constant = "0.0") // TODO: Integrar com ParametroGlobal
    @Mapping(target = "impostosFaturamentoEstimados", constant = "0.0")
    @Mapping(target = "precoFinalSugerido", source = "precoFinalSugerido")
    @Mapping(target = "dataEmissao", source = "dataEmissao")
    OrcamentoResponseDTO toDto(Orcamento orcamento);

    default List<OrcamentoResponseDTO.ServicoAdicionalDTO> mapServicos(List<OrcamentoAC> servicos) {
        if (servicos == null) return null;
        return servicos.stream()
                .map(s -> new OrcamentoResponseDTO.ServicoAdicionalDTO(
                        s.getDescricaoServico(),
                        s.getQuantidadeHoras(),
                        s.getCustoTotalAC()
                ))
                .collect(Collectors.toList());
    }

    default java.math.BigDecimal calcularTotalAC(List<OrcamentoAC> servicos) {
        if (servicos == null) return java.math.BigDecimal.ZERO;
        return servicos.stream()
                .map(OrcamentoAC::getCustoTotalAC)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
    }
}
