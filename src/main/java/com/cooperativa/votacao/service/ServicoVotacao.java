package com.cooperativa.votacao.service;

import com.cooperativa.votacao.dto.ResultadoVotacaoDTO;
import com.cooperativa.votacao.dto.VotoRequisicaoDTO;
import com.cooperativa.votacao.dto.VotoRespostaDTO;
import com.cooperativa.votacao.excecao.SessaoFechadaException;
import com.cooperativa.votacao.excecao.VotoJaRegistradoException;
import com.cooperativa.votacao.model.SessaoVotacao;
import com.cooperativa.votacao.model.TipoVoto;
import com.cooperativa.votacao.model.Voto;
import com.cooperativa.votacao.repository.RepositorioVoto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ServicoVotacao {

    private static final Logger logger = LoggerFactory.getLogger(ServicoVotacao.class);
    private final RepositorioVoto repositorioVoto;
    private final ServicoSessaoVotacao servicoSessaoVotacao;

    public ServicoVotacao(RepositorioVoto repositorioVoto, ServicoSessaoVotacao servicoSessaoVotacao) {
        this.repositorioVoto = repositorioVoto;
        this.servicoSessaoVotacao = servicoSessaoVotacao;
    }

    public VotoRespostaDTO registrarVoto(VotoRequisicaoDTO requisicao) {
        SessaoVotacao sessao = servicoSessaoVotacao.buscarEntidadeSessao(requisicao.sessaoVotacaoId());

        if (!sessao.estaAberta()) {
            throw new SessaoFechadaException();
        }

        if (repositorioVoto.existsBySessaoVotacaoIdAndAssociadoId(requisicao.sessaoVotacaoId(), requisicao.associadoId())) {
            throw new VotoJaRegistradoException(requisicao.associadoId(), requisicao.sessaoVotacaoId());
        }

        Voto votoSalvo = repositorioVoto.save(new Voto(sessao, requisicao.associadoId(), requisicao.opcaoVoto()));
        logger.info("Voto {} registrado na sessão {} pelo associado {}", votoSalvo.getId(), sessao.getId(), votoSalvo.getAssociadoId());
        return new VotoRespostaDTO(votoSalvo.getId(), sessao.getId(), votoSalvo.getAssociadoId(), votoSalvo.getOpcaoVoto(), votoSalvo.getDataVoto());
    }

    @Transactional(readOnly = true)
    public ResultadoVotacaoDTO contabilizarVotos(Long sessaoId) {
        servicoSessaoVotacao.fecharSessoesExpiradas(); // verificação de expiração
        SessaoVotacao sessao = servicoSessaoVotacao.buscarEntidadeSessao(sessaoId);

        long votosSim = repositorioVoto.countBySessaoVotacaoIdAndOpcaoVoto(sessaoId, TipoVoto.SIM);
        long votosNao = repositorioVoto.countBySessaoVotacaoIdAndOpcaoVoto(sessaoId, TipoVoto.NAO);
        long totalVotos = votosSim + votosNao;

        String resultado = determinarResultado(votosSim, votosNao);
        return new ResultadoVotacaoDTO(sessao.getId(), sessao.getPauta().getId(), sessao.getPauta().getTitulo(),
            totalVotos, votosSim, votosNao, resultado, !sessao.estaAberta());
    }

    private String determinarResultado(long votosSim, long votosNao) {
        return (votosSim > votosNao) ? "APROVADO" : (votosNao > votosSim) ? "REPROVADO" : "EMPATE";
    }
}