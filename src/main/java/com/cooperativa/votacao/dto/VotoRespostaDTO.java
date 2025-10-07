package com.cooperativa.votacao.dto;

import java.time.LocalDateTime;

import com.cooperativa.votacao.model.TipoVoto;

public record VotoRespostaDTO(
    Long id,
    Long sessaoVotacaoId,
    String associadoId,
    TipoVoto opcaoVoto,
    LocalDateTime dataVoto
) {}