package com.cooperativa.votacao.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SessaoVotacaoRequisicaoDTO(
    @NotNull Long pautaId,
    @Min(1) Integer duracaoMinutos
) {
    public SessaoVotacaoRequisicaoDTO {
        if (duracaoMinutos == null) duracaoMinutos = 1;
    }
}