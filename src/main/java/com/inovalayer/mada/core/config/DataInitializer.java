package com.inovalayer.mada.core.config;

import com.inovalayer.mada.core.domain.Usuario;
import com.inovalayer.mada.core.domain.UsuarioRole;
import com.inovalayer.mada.core.repository.ClienteRepository;
import com.inovalayer.mada.core.repository.UsuarioRepository;
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
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        seedUsuarios();
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
            
            // 2. Cliente de Teste
            Usuario uCliente = new Usuario();
            uCliente.setEmail("cliente@empresa.com");
            uCliente.setSenha(passwordEncoder.encode("cliente123"));
            uCliente.setNomeCompleto("João Silva (Gestor)");
            uCliente.setRole(UsuarioRole.CLIENTE);
            usuarioRepository.save(uCliente);

            // Criar Entidade Cliente vinculada ao Usuario
            com.inovalayer.mada.core.domain.Cliente cEntity = new com.inovalayer.mada.core.domain.Cliente();
            cEntity.setUsuario(uCliente);
            cEntity.setRazaoSocial("InovaLayer Client Corporation S/A");
            cEntity.setCnpj("12345678000199");
            cEntity.setEmailContato("cliente@empresa.com");
            cEntity.setSetorAtuacao("Aeroespacial / Defesa");
            cEntity.setVip(true);
            clienteRepository.save(cEntity);

            log.info("Usuário CLIENTE e Entidade CLIENTE criados.");
            log.info("Semeadura de usuários concluída com sucesso.");
        } else {
            log.info("Usuários já existentes na base. Pulando semeadura.");
        }
    }
}
