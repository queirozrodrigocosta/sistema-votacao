package com.cooperativa.votacao.excecao;

public class SessaoNaoEncontradaException extends ExcecaoNegocio {
    public SessaoNaoEncontradaException(Long id) {
        super("Sessão de votação não encontrada com ID: " + id);
    }
}