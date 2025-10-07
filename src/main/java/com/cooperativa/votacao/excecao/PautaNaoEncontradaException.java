package com.cooperativa.votacao.excecao;

public class PautaNaoEncontradaException extends ExcecaoNegocio {
    public PautaNaoEncontradaException(Long id) {
        super("Pauta não encontrada com ID: " + id);
    }
}