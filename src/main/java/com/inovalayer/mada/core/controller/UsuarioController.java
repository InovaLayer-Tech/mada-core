package com.inovalayer.mada.core.controller;

import com.inovalayer.mada.core.domain.Usuario;
import com.inovalayer.mada.core.dto.CadastroRequestDTO;
import com.inovalayer.mada.core.dto.UsuarioResponseDTO;
import com.inovalayer.mada.core.repository.UsuarioRepository;
import com.inovalayer.mada.core.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final AuthService authService;

    @GetMapping("/perfil")
    public ResponseEntity<UsuarioResponseDTO> obterPerfil() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        return ResponseEntity.ok(new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getNomeCompleto(),
                usuario.getRole()
        ));
    }

    @PutMapping("/perfil")
    public ResponseEntity<UsuarioResponseDTO> atualizarPerfil(@RequestBody UsuarioResponseDTO dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        if (dto.nomeCompleto() != null) {
            usuario.setNomeCompleto(dto.nomeCompleto());
        }

        if (dto.email() != null && !dto.email().isBlank() && !dto.email().equals(usuario.getEmail())) {
            if (usuarioRepository.findByEmail(dto.email()).isPresent()) {
                throw new RuntimeException("Este e-mail já está em uso por outro usuário.");
            }
            usuario.setEmail(dto.email());
        }
        
        usuarioRepository.save(usuario);
        
        return ResponseEntity.ok(new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getNomeCompleto(),
                usuario.getRole()
        ));
    }

    @PutMapping("/perfil/senha")
    public ResponseEntity<?> atualizarSenha(@RequestBody com.inovalayer.mada.core.dto.SenhaUpdateDTO dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        authService.atualizarSenha(email, dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> criarAdmin(@RequestBody CadastroRequestDTO request) {
        // Aproveita a lógica de cadastro, mas com role ADMIN
        // Precisamos adicionar um método cadastrarAdmin no AuthService ou parametrizar o cadastrar
        return ResponseEntity.ok(authService.cadastrarAdmin(request));
    }
}
