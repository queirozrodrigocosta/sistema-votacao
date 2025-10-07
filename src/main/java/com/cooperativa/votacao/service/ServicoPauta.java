package com.cooperativa.votacao.service;

import com.cooperativa.votacao.dto.PautaRequisicaoDTO;
import com.cooperativa.votacao.dto.PautaRespostaDTO;
import com.cooperativa.votacao.excecao.PautaNaoEncontradaException;
import com.cooperativa.votacao.model.Pauta;
import com.cooperativa.votacao.repository.RepositorioPauta;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ServicoPauta {

    private static final Logger logger = LoggerFactory.getLogger(ServicoPauta.class);
    private final RepositorioPauta repositorioPauta;

    public ServicoPauta(RepositorioPauta repositorioPauta) {
        this.repositorioPauta = repositorioPauta;
    }

    public PautaRespostaDTO criarPauta(PautaRequisicaoDTO requisicao) {
        logger.info("Criando pauta: {}", requisicao.titulo());
        Pauta pauta = repositorioPauta.save(new Pauta(requisicao.titulo(), requisicao.descricao()));
        return new PautaRespostaDTO(pauta.getId(), pauta.getTitulo(), pauta.getDescricao(), pauta.getDataCriacao());
    }

    @Transactional(readOnly = true)
    public PautaRespostaDTO buscarPauta(Long id) {
        Pauta pauta = buscarEntidadePauta(id);
        return new PautaRespostaDTO(pauta.getId(), pauta.getTitulo(), pauta.getDescricao(), pauta.getDataCriacao());
    }

    @Transactional(readOnly = true)
    public List<PautaRespostaDTO> listarPautas() {
        return repositorioPauta.findAll().stream()
            .map(p -> new PautaRespostaDTO(p.getId(), p.getTitulo(), p.getDescricao(), p.getDataCriacao()))
            .toList();
    }

    @Transactional(readOnly = true)
    public List<PautaRespostaDTO> buscarPautasPorTitulo(String titulo) {
        return repositorioPauta.findByTituloContainingIgnoreCase(titulo).stream()
            .map(p -> new PautaRespostaDTO(p.getId(), p.getTitulo(), p.getDescricao(), p.getDataCriacao()))
            .toList();
    }

    public Pauta buscarEntidadePauta(Long id) {
        return repositorioPauta.findById(id).orElseThrow(() -> new PautaNaoEncontradaException(id));
    }
}