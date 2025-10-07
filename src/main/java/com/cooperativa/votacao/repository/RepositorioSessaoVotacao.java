package com.cooperativa.votacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cooperativa.votacao.model.SessaoVotacao;
import com.cooperativa.votacao.model.StatusSessao;

import java.util.List;
import java.util.Optional;

public interface RepositorioSessaoVotacao extends JpaRepository<SessaoVotacao, Long> {
    List<SessaoVotacao> findByStatus(StatusSessao status);

    Optional<SessaoVotacao> findByPautaIdAndStatus(Long pautaId, StatusSessao status);

    @Query("SELECT s FROM SessaoVotacao s WHERE s.pauta.id = :pautaId ORDER BY s.dataAbertura DESC")
    List<SessaoVotacao> findByPautaIdOrderByDataAberturaDesc(Long pautaId);
}