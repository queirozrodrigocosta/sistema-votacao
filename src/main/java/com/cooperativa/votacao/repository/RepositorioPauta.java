package com.cooperativa.votacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooperativa.votacao.model.Pauta;

import java.util.List;

@Repository
public interface RepositorioPauta extends JpaRepository<Pauta, Long> {
    List<Pauta> findByTituloContainingIgnoreCase(String titulo);
}