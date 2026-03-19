package com.inovalayer.mada.core.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidade que representa os clientes da InovaLayer para os quais os orçamentos são gerados.
 */
@Getter
@Setter
@Entity
@Table(name = "tb_cliente") // O prefixo tb_ organiza visualmente o banco de dados
public class Cliente extends BaseEntity {

    @jakarta.persistence.OneToOne
    @jakarta.persistence.JoinColumn(name = "usuario_id")
    private Usuario usuario;

    // Defino tamanho máximo e garanto que a razão social não pode ser nula
    @Column(name = "razao_social", nullable = false, length = 150)
    private String razaoSocial;

    // CNPJ pode ser preenchido posteriormente no perfil.
    @Column(name = "cnpj", nullable = true, unique = true, length = 14)
    private String cnpj;

    @Column(name = "email_contato", length = 100)
    private String emailContato;

    @Column(name = "setor_atuacao", length = 100)
    private String setorAtuacao;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;

    @Column(name = "vip", nullable = false)
    private Boolean vip = false;
    
    // O relacionamento com Orcamento (OneToMany) será feito na entidade Orcamento (ManyToOne) 
    // para mantermos a chave estrangeira do lado correto e evitarmos tabelas associativas desnecessárias.
}