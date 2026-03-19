package com.inovalayer.mada.core.service;

import com.inovalayer.mada.core.domain.ParametroGlobal;
import com.inovalayer.mada.core.repository.ParametroGlobalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Serviço responsável por gerenciar as configurações globais de custos e margens.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ParametroGlobalService {

    private final ParametroGlobalRepository repository;

    @Transactional(readOnly = true)
    public ParametroGlobal obterConfiguracao() {
        return repository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Configuração Global não encontrada no Banco de Dados."));
    }

    @Transactional
    public ParametroGlobal atualizar(ParametroGlobal novosDados) {
        log.info("ATUALIZANDO CONFIGURAÇÕES GLOBAIS DE TAXAS E MARGENS");
        ParametroGlobal atual = obterConfiguracao();
        
        atual.setCustoKwh(novosDados.getCustoKwh());
        atual.setTaxaMaoDeObraHora(novosDados.getTaxaMaoDeObraHora());
        atual.setTaxaEngenheiroHora(novosDados.getTaxaEngenheiroHora());
        atual.setTaxaUsinagemHora(novosDados.getTaxaUsinagemHora());
        atual.setTaxaDepreciacaoMaquinaHora(novosDados.getTaxaDepreciacaoMaquinaHora());
        atual.setCustoTratamentoTermicoFixo(novosDados.getCustoTratamentoTermicoFixo());
        atual.setFatorRiscoK1(novosDados.getFatorRiscoK1());
        atual.setFatorRiscoK2(novosDados.getFatorRiscoK2());
        atual.setConsumoPotenciaKw(novosDados.getConsumoPotenciaKw());
        atual.setMargemLucroPercentual(novosDados.getMargemLucroPercentual());
        atual.setTaxaImpostos(novosDados.getTaxaImpostos());

        ParametroGlobal saved = repository.save(atual);
        log.info("CONFIGURAÇÕES GLOBAIS ATUALIZADAS COM SUCESSO: Margem: {}%, Impostos: {}%", 
                saved.getMargemLucroPercentual(), saved.getTaxaImpostos());
        
        return saved;
    }
}
