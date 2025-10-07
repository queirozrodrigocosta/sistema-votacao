package com.cooperativa.votacao.servico;

import com.cooperativa.votacao.dto.PautaRequisicaoDTO;
import com.cooperativa.votacao.dto.PautaRespostaDTO;
import com.cooperativa.votacao.excecao.PautaNaoEncontradaException;
import com.cooperativa.votacao.model.Pauta;
import com.cooperativa.votacao.repository.RepositorioPauta;
import com.cooperativa.votacao.service.ServicoPauta;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TesteServicoPauta {

    @Mock
    RepositorioPauta repositorioPauta;

    @InjectMocks
    ServicoPauta servicoPauta;

    @Test
    void deveCriarPauta() {
        when(repositorioPauta.save(any(Pauta.class))).thenReturn(new Pauta("titulo", "descricao"));

        PautaRespostaDTO resultado = servicoPauta.criarPauta(new PautaRequisicaoDTO("titulo", "descricao"));

        assertThat(resultado).isNotNull();
        assertThat(resultado.titulo()).isEqualTo("titulo");
        verify(repositorioPauta).save(any(Pauta.class));
    }

    @Test
    void deveBuscarPautaPorId() {
        Pauta pauta = new Pauta("titulo", "descricao");
        when(repositorioPauta.findById(1L)).thenReturn(Optional.of(pauta));

        PautaRespostaDTO resultado = servicoPauta.buscarPauta(1L);

        assertThat(resultado).isNotNull();
        verify(repositorioPauta).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoPautaNaoEncontrada() {
        when(repositorioPauta.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> servicoPauta.buscarPauta(99L)).isInstanceOf(PautaNaoEncontradaException.class);
    }

    @Test
    void deveListarTodasAsPautas() {
        when(repositorioPauta.findAll()).thenReturn(List.of(new Pauta("titulo", "descricao")));
        var lista = servicoPauta.listarPautas();
        assertThat(lista).hasSize(1);
    }
}