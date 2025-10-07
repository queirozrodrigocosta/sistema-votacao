package com.cooperativa.votacao.dto;

public record ResultadoVotacaoDTO(
    Long sessaoId,
    Long pautaId,
    String pautaTitulo,
    long totalVotos,
    long votosSim,
    long votosNao,
    String resultado,
    boolean sessaoFechada
) {}