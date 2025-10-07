package com.cooperativa.votacao.excecao;

public class VotoJaRegistradoException extends ExcecaoNegocio {
    public VotoJaRegistradoException(String associadoId, Long sessaoId) {
        super("Associado " + associadoId + " já votou na sessão " + sessaoId);
    }
}