package com.cooperativa.votacao.excecao;

public class ExcecaoNegocio extends RuntimeException {
    public ExcecaoNegocio(String mensagem) { super(mensagem); }
    public ExcecaoNegocio(String mensagem, Throwable causa) { super(mensagem, causa); }
}