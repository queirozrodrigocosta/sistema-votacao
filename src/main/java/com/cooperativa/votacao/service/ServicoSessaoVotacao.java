package com.cooperativa.votacao.service;

import com.cooperativa.votacao.dto.SessaoVotacaoRequisicaoDTO;
import com.cooperativa.votacao.dto.SessaoVotacaoRespostaDTO;
import com.cooperativa.votacao.excecao.ExcecaoNegocio;
import com.cooperativa.votacao.excecao.SessaoNaoEncontradaException;
import com.cooperativa.votacao.model.Pauta;
import com.cooperativa.votacao.model.SessaoVotacao;
import com.cooperativa.votacao.model.StatusSessao;
import com.cooperativa.votacao.repository.RepositorioSessaoVotacao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ServicoSessaoVotacao {

    private static final Logger logger = LoggerFactory.getLogger(ServicoSessaoVotacao.class);
    private final RepositorioSessaoVotacao repositorioSessao;
    private final ServicoPauta servicoPauta;

    public ServicoSessaoVotacao(RepositorioSessaoVotacao repositorioSessao, ServicoPauta servicoPauta) {
        this.repositorioSessao = repositorioSessao;
        this.servicoPauta = servicoPauta;
    }

    public SessaoVotacaoRespostaDTO abrirSessao(SessaoVotacaoRequisicaoDTO requisicao) {
        Pauta pauta = servicoPauta.buscarEntidadePauta(requisicao.pautaId());

        Optional<SessaoVotacao> sessaoAberta = repositorioSessao.findByPautaIdAndStatus(requisicao.pautaId(), StatusSessao.ABERTA);
        if (sessaoAberta.isPresent()) {
            throw new ExcecaoNegocio("Já existe uma sessão aberta para esta pauta");
        }

        int minutos = requisicao.duracaoMinutos() != null ? requisicao.duracaoMinutos() : 1;
        SessaoVotacao sessao = repositorioSessao.save(new SessaoVotacao(pauta, minutos));
        logger.info("Sessão {} aberta para pauta {} por {} minutos", sessao.getId(), pauta.getId(), minutos);
        return mapearParaResposta(sessao);
    }

    @Transactional(readOnly = true)
    public SessaoVotacaoRespostaDTO buscarSessao(Long id) {
        return mapearParaResposta(buscarEntidadeSessao(id));
    }

    @Transactional(readOnly = true)
    public List<SessaoVotacaoRespostaDTO> listarSessoes() {
        return repositorioSessao.findAll().stream().map(this::mapearParaResposta).toList();
    }

    @Transactional(readOnly = true)
    public List<SessaoVotacaoRespostaDTO> listarSessoesPorPauta(Long pautaId) {
        return repositorioSessao.findByPautaIdOrderByDataAberturaDesc(pautaId).stream().map(this::mapearParaResposta).toList();
    }

    // Fecha sessões expiradas a cada 30 segundos
    @Scheduled(fixedRate = 30000)
    public void fecharSessoesExpiradas() {
        List<SessaoVotacao> sessoesAbertas = repositorioSessao.findByStatus(StatusSessao.ABERTA);
        LocalDateTime agora = LocalDateTime.now();
        for (SessaoVotacao sessao : sessoesAbertas) {
            if (agora.isAfter(sessao.getDataFechamento())) {
                sessao.fechar();
                repositorioSessao.save(sessao);
                logger.info("Sessão {} fechada automaticamente por expiração", sessao.getId());
            }
        }
    }

    public SessaoVotacao buscarEntidadeSessao(Long id) {
        return repositorioSessao.findById(id).orElseThrow(() -> new SessaoNaoEncontradaException(id));
    }

    private SessaoVotacaoRespostaDTO mapearParaResposta(SessaoVotacao sessao) {
        return new SessaoVotacaoRespostaDTO(
            sessao.getId(),
            sessao.getPauta().getId(),
            sessao.getPauta().getTitulo(),
            sessao.getDataAbertura(),
            sessao.getDataFechamento(),
            sessao.getStatus(),
            sessao.estaAberta()
        );
    }
}