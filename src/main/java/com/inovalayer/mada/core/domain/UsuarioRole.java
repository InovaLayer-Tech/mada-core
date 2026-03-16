package com.inovalayer.mada.core.domain;

/**
 * Papéis de acesso do sistema InovaLayer 3D.
 */
public enum UsuarioRole {
    ADMIN("admin"),
    CLIENTE("cliente");

    private String role;

    UsuarioRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
