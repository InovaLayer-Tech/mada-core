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
        usuario.setRole(UsuarioRole.CLIENTE); // Todo cadastro via portal é CLIENTE por padrão
        
        // Aqui poderíamos salvar campos adicionais como nomeEmpresa no futuro se a entidade Usuario permitir
        // Por enquanto, Usuario só tem email, senha e role.
        
        usuarioRepository.save(usuario);

        String jwtToken = jwtService.gerarToken(usuario);

        return AuthResponseDTO.builder()
                .token(jwtToken)
                .email(usuario.getEmail())
                .role(usuario.getRole().name())
                .expiresAt(jwtService.getExpirationTime())
                .build();
    }

    public AuthResponseDTO login(LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getSenha()
                )
        );

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("E-mail ou senha inválidos"));

        String jwtToken = jwtService.gerarToken(usuario);

        return AuthResponseDTO.builder()
                .token(jwtToken)
                .email(usuario.getEmail())
                .role(usuario.getRole().name())
                .expiresAt(jwtService.getExpirationTime())
                .build();
    }
}
