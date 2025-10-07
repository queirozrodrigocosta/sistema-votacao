package com.cooperativa.votacao.dto;

import java.time.LocalDateTime;

import com.cooperativa.votacao.model.StatusSessao;

public record SessaoVotacaoRespostaDTO(
    Long id,
    Long pautaId,
    String pautaTitulo,
    LocalDateTime dataAbertura,
    LocalDateTime dataFechamento,
    StatusSessao status,
    boolean aberta
) {}