package com.inovalayer.mada.core.service;

import com.inovalayer.mada.core.domain.ParametroGlobal;
import com.inovalayer.mada.core.repository.ParametroGlobalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ParametroGlobalService {

    private final ParametroGlobalRepository repository;

    @Transactional(readOnly = true)
    public ParametroGlobal obterConfiguracao() {
        // Como é um Singleton-like, pegamos o primeiro registro.
        // Se não existir, deveríamos ter um seed, mas para o MVP pegamos o que houver.
        return repository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Configuração Global não encontrada no Banco de Dados."));
    }

    @Transactional
    public ParametroGlobal atualizar(ParametroGlobal novosDados) {
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

        return repository.save(atual);
    }
}
