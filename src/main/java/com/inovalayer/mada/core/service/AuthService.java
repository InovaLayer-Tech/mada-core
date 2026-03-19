package com.inovalayer.mada.core.service;

import com.inovalayer.mada.core.domain.Usuario;
import com.inovalayer.mada.core.domain.UsuarioRole;
import com.inovalayer.mada.core.dto.AuthResponseDTO;
import com.inovalayer.mada.core.dto.CadastroRequestDTO;
import com.inovalayer.mada.core.dto.LoginRequestDTO;
import com.inovalayer.mada.core.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Serviço responsável por orquestrar a autenticação e o cadastro de usuários.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ClienteService clienteService;

    @Transactional
    public AuthResponseDTO cadastrar(CadastroRequestDTO request) {
        if (!request.getSenha().equals(request.getConfirmacaoSenha())) {
            throw new IllegalArgumentException("As senhas não coincidem");
        }

        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Este e-mail já está cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(request.getEmail());
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        usuario.setNomeCompleto(request.getNomeCompleto());
        usuario.setRole(UsuarioRole.CLIENTE);
        
        usuarioRepository.save(usuario);

        // Criar entidade Cliente vinculada
        com.inovalayer.mada.core.domain.Cliente cliente = new com.inovalayer.mada.core.domain.Cliente();
        cliente.setUsuario(usuario);
        cliente.setRazaoSocial(request.getNomeEmpresa());
        cliente.setEmailContato(request.getEmail());
        // CNPJ ficará em branco até o cliente preencher no perfil, ou podemos exigir no DTO
        // Por enquanto vamos permitir vazio para não quebrar o fluxo atual se o front não enviar
        
        clienteService.salvar(cliente);

        String jwtToken = jwtService.gerarToken(usuario);

        return AuthResponseDTO.builder()
                .token(jwtToken)
                .email(usuario.getEmail())
                .nomeCompleto(usuario.getNomeCompleto())
                .role(usuario.getRole().name())
                .expiresAt(jwtService.getExpirationTime())
                .build();
    }

    public AuthResponseDTO login(LoginRequestDTO request) {
        System.out.println("DEBUG [AuthService] Tentativa de login para: " + request.getEmail());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getSenha()
                    )
            );
            System.out.println("DEBUG [AuthService] Autenticação bem-sucedida para: " + request.getEmail());
        } catch (Exception e) {
            System.err.println("DEBUG [AuthService] Erro na autenticação para " + request.getEmail() + ": " + e.getMessage());
            throw e;
        }

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("E-mail ou senha inválidos"));

        String jwtToken = jwtService.gerarToken(usuario);
        System.out.println("DEBUG [AuthService] Token gerado com sucesso para: " + request.getEmail());

        return AuthResponseDTO.builder()
                .token(jwtToken)
                .email(usuario.getEmail())
                .nomeCompleto(usuario.getNomeCompleto())
                .role(usuario.getRole().name())
                .expiresAt(jwtService.getExpirationTime())
                .build();
    }

    @Transactional
    public AuthResponseDTO cadastrarAdmin(CadastroRequestDTO request) {
        if (!request.getSenha().equals(request.getConfirmacaoSenha())) {
            throw new IllegalArgumentException("As senhas não coincidem");
        }

        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Este e-mail já está cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(request.getEmail());
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        usuario.setNomeCompleto(request.getNomeCompleto());
        usuario.setRole(UsuarioRole.ADMIN);
        
        usuarioRepository.save(usuario);

        return AuthResponseDTO.builder()
                .email(usuario.getEmail())
                .role(usuario.getRole().name())
                .build();
    }

    @Transactional
    public void atualizarSenha(String email, com.inovalayer.mada.core.dto.SenhaUpdateDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        if (!passwordEncoder.matches(dto.senhaAntiga(), usuario.getSenha())) {
            throw new IllegalArgumentException("A senha atual está incorreta");
        }

        if (!dto.novaSenha().equals(dto.confirmacaoSenha())) {
            throw new IllegalArgumentException("A nova senha e a confirmação não coincidem");
        }

        usuario.setSenha(passwordEncoder.encode(dto.novaSenha()));
        usuarioRepository.save(usuario);
    }
}
