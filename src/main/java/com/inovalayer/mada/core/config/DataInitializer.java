package com.inovalayer.mada.core.config;

import com.inovalayer.mada.core.domain.*;
import com.inovalayer.mada.core.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Inicializador de dados para garantir que o sistema tenha usuários padrão para teste.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final ParametroGlobalRepository parametroGlobalRepository;
    private final ArameMetalicoRepository arameMetalicoRepository;
    private final GasProtecaoRepository gasProtecaoRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        seedUsuarios();
        seedParametrosGlobais();
        seedInsumos();
        seedClienteTesteErmetal();
    }

    private void seedUsuarios() {
        if (usuarioRepository.count() == 0) {
            log.info("Iniciando semeadura de usuários padrão...");

            // 1. Admin Padrão
            Usuario admin = new Usuario();
            admin.setEmail("admin@inovalayer.com");
            admin.setSenha(passwordEncoder.encode("admin123"));
            admin.setNomeCompleto("Administrador do Sistema");
            admin.setRole(UsuarioRole.ADMIN);
            usuarioRepository.save(admin);
            log.info("Usuário ADMIN criado: admin@inovalayer.com / admin123");
            
            // 2. Cliente de Teste Padrão
            Usuario uCliente = new Usuario();
            uCliente.setEmail("cliente@empresa.com");
            uCliente.setSenha(passwordEncoder.encode("cliente123"));
            uCliente.setNomeCompleto("João Silva (Gestor)");
            uCliente.setRole(UsuarioRole.CLIENTE);
            usuarioRepository.save(uCliente);

            Cliente cEntity = new Cliente();
            cEntity.setUsuario(uCliente);
            cEntity.setRazaoSocial("InovaLayer Client Corporation S/A");
            cEntity.setCnpj("11223344000155");
            cEntity.setEmailContato("cliente@empresa.com");
            cEntity.setSetorAtuacao("Aeroespacial / Defesa");
            cEntity.setVip(true);
            clienteRepository.save(cEntity);

            log.info("Usuário CLIENTE e Entidade CLIENTE criados.");
        }
    }

    private void seedParametrosGlobais() {
        if (parametroGlobalRepository.count() == 0) {
            log.info("Injetando Parâmetros Globais (Cérebro Financeiro)...");
            ParametroGlobal p = new ParametroGlobal();
            p.setCustoKwh(new java.math.BigDecimal("0.85"));
            p.setTaxaMaoDeObraHora(new java.math.BigDecimal("45.00"));
            p.setTaxaEngenheiroHora(new java.math.BigDecimal("120.00"));
            p.setTaxaUsinagemHora(new java.math.BigDecimal("85.00")); // Default
            p.setTaxaDepreciacaoMaquinaHora(new java.math.BigDecimal("15.50"));
            p.setCustoTratamentoTermicoFixo(new java.math.BigDecimal("250.00")); // Default
            p.setFatorRiscoK1(new java.math.BigDecimal("1.10")); // Default
            p.setFatorRiscoK2(new java.math.BigDecimal("1.05")); // Default
            p.setConsumoPotenciaKw(new java.math.BigDecimal("5.50"));
            p.setMargemLucroPercentual(new java.math.BigDecimal("30.00"));
            p.setTaxaImpostos(new java.math.BigDecimal("15.00"));
            parametroGlobalRepository.save(p);
        }
    }

    private void seedInsumos() {
        if (arameMetalicoRepository.count() == 0) {
            log.info("Inserindo Catálogo de Arames Metálicos...");
            ArameMetalico arame = new ArameMetalico();
            arame.setNome("ER70S-6 (Aço Carbono)");
            arame.setFornecedor("Gerdau");
            arame.setCodigoProduto("W-ER70S6-01");
            arame.setPrecoUnitarioBase(new java.math.BigDecimal("38.50"));
            arame.setDensidadeGcm3(7.85);
            arame.setEficiencia(95.00);
            arame.setLigaMetalica("Aço Carbono");
            arame.setTipoMaterial("Aço");
            arameMetalicoRepository.save(arame);
        }

        if (gasProtecaoRepository.count() == 0) {
            log.info("Inserindo Catálogo de Gás de Proteção...");
            GasProtecao gas = new GasProtecao();
            gas.setNome("Mistura Ar/CO2 (80/20)");
            gas.setFornecedor("GasSolution");
            gas.setCodigoProduto("G-C20-01");
            gas.setPrecoUnitarioBase(new java.math.BigDecimal("30.00"));
            gas.setTipoGas("C20");
            gas.setVazaoPadrao(15.00);
            gasProtecaoRepository.save(gas);
        }
    }

    private void seedClienteTesteErmetal() {
        if (!usuarioRepository.existsByEmail("engenharia@ermetal.com")) {
            log.info("Criando Cliente B2C de Teste: Ermetal...");
            Usuario uErmetal = new Usuario();
            uErmetal.setEmail("engenharia@ermetal.com");
            uErmetal.setSenha(passwordEncoder.encode("ermetal123"));
            uErmetal.setNomeCompleto("Eng. Chefe Teste (Ermetal)");
            uErmetal.setRole(UsuarioRole.CLIENTE);
            usuarioRepository.save(uErmetal);

            Cliente cErmetal = new Cliente();
            cErmetal.setUsuario(uErmetal);
            cErmetal.setRazaoSocial("Ermetal Engenharia Automotiva");
            cErmetal.setCnpj("12345678000199");
            cErmetal.setEmailContato("engenharia@ermetal.com");
            cErmetal.setSetorAtuacao("Automotivo");
            cErmetal.setVip(true);
            clienteRepository.save(cErmetal);
            log.info("Cliente Ermetal e usuário associado criados.");
        }
    }
}
