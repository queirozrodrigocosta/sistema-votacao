package com.cooperativa.votacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cooperativa.votacao.model.TipoVoto;
import com.cooperativa.votacao.model.Voto;

public interface RepositorioVoto extends JpaRepository<Voto, Long> {
    boolean existsBySessaoVotacaoIdAndAssociadoId(Long sessaoVotacaoId, String associadoId);

    @Query("SELECT COUNT(v) FROM Voto v WHERE v.sessaoVotacao.id = :sessaoId AND v.opcaoVoto = :tipoVoto")
    long countBySessaoVotacaoIdAndOpcaoVoto(Long sessaoId, TipoVoto tipoVoto);
}