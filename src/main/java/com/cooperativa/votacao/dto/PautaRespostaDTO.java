package com.cooperativa.votacao.dto;

import java.time.LocalDateTime;

public record PautaRespostaDTO(
    Long id,
    String titulo,
    String descricao,
    LocalDateTime dataCriacao
) {}