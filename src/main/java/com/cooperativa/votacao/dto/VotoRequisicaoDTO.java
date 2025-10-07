package com.cooperativa.votacao.dto;

import com.cooperativa.votacao.model.TipoVoto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VotoRequisicaoDTO(
    @NotNull Long sessaoVotacaoId,
    @NotBlank String associadoId,
    @NotNull TipoVoto opcaoVoto
) {}