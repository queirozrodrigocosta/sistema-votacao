package com.cooperativa.votacao.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sessoes_votacao")
public class SessaoVotacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "pauta_id")
    private Pauta pauta;

    @Column(name = "data_abertura", nullable = false)
    private LocalDateTime dataAbertura;

    @Column(name = "data_fechamento", nullable = false)
    private LocalDateTime dataFechamento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusSessao status;

    @OneToMany(mappedBy = "sessaoVotacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Voto> votos = new ArrayList<>();

    protected SessaoVotacao() {}

    public SessaoVotacao(Pauta pauta, int duracaoMinutos) {
        this.pauta = pauta;
        this.dataAbertura = LocalDateTime.now();
        this.dataFechamento = this.dataAbertura.plusMinutes(duracaoMinutos);
        this.status = StatusSessao.ABERTA;
    }

    public Long getId() {
        return id;
    }

    public Pauta getPauta() {
        return pauta;
    }

    public void setPauta(Pauta pauta) {
        this.pauta = pauta;
    }

    public LocalDateTime getDataAbertura() {
        return dataAbertura;
    }

    public LocalDateTime getDataFechamento() {
        return dataFechamento;
    }

    public StatusSessao getStatus() {
        return status;
    }

    public void setStatus(StatusSessao status) {
        this.status = status;
    }

    public List<Voto> getVotos() {
        return votos;
    }

    public void adicionarVoto(Voto voto) {
        this.votos.add(voto);
        voto.setSessaoVotacao(this);
    }

    public boolean estaAberta() {
        return status == StatusSessao.ABERTA && LocalDateTime.now().isBefore(dataFechamento);
    }

    public void fechar() {
        this.status = StatusSessao.FECHADA;
    }
}