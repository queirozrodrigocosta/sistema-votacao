package com.cooperativa.votacao.excecao;

public class SessaoFechadaException extends ExcecaoNegocio {
    public SessaoFechadaException() {
        super("A sessão de votação está fechada");
    }
}