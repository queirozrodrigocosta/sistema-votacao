package com.cooperativa.votacao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PautaRequisicaoDTO(
    @NotBlank @Size(max = 200) String titulo,
    @NotBlank @Size(max = 1000) String descricao
) {}