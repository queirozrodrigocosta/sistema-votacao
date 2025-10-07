package com.cooperativa.votacao.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "votos", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"sessao_votacao_id", "associado_id"})
})
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "sessao_votacao_id")
    private SessaoVotacao sessaoVotacao;

    @NotNull
    @Column(name = "associado_id", nullable = false)
    private String associadoId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoVoto opcaoVoto;

    @Column(name = "data_voto", nullable = false)
    private LocalDateTime dataVoto;

    protected Voto() {}

    public Voto(SessaoVotacao sessaoVotacao, String associadoId, TipoVoto opcaoVoto) {
        this.sessaoVotacao = sessaoVotacao;
        this.associadoId = associadoId;
        this.opcaoVoto = opcaoVoto;
        this.dataVoto = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public SessaoVotacao getSessaoVotacao() {
        return sessaoVotacao;
    }

    public void setSessaoVotacao(SessaoVotacao sessaoVotacao) {
        this.sessaoVotacao = sessaoVotacao;
    }

    public String getAssociadoId() {
        return associadoId;
    }

    public void setAssociadoId(String associadoId) {
        this.associadoId = associadoId;
    }

    public TipoVoto getOpcaoVoto() {
        return opcaoVoto;
    }

    public void setOpcaoVoto(TipoVoto opcaoVoto) {
        this.opcaoVoto = opcaoVoto;
    }

    public LocalDateTime getDataVoto() {
        return dataVoto;
    }
}