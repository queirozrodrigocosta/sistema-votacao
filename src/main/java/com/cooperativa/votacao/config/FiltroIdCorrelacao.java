package com.cooperativa.votacao.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class FiltroIdCorrelacao extends OncePerRequestFilter {
    public static final String CABECALHO = "X-Correlation-Id";
    public static final String CHAVE_MDC = "1";

    @Override
    protected void doFilterInternal(HttpServletRequest requisicao, HttpServletResponse resposta, FilterChain cadeiaFiltros)
            throws ServletException, IOException {
        String idCorrelacao = requisicao.getHeader(CABECALHO);
        if (idCorrelacao == null || idCorrelacao.isBlank()) {
            idCorrelacao = UUID.randomUUID().toString();
        }
        MDC.put(CHAVE_MDC, idCorrelacao);
        resposta.setHeader(CABECALHO, idCorrelacao);
        try {
            cadeiaFiltros.doFilter(requisicao, resposta);
        } finally {
            MDC.remove(CHAVE_MDC);
        }
    }
}