package com.cooperativa.votacao.servico;

import com.cooperativa.votacao.dto.ResultadoVotacaoDTO;
import com.cooperativa.votacao.dto.VotoRequisicaoDTO;
import com.cooperativa.votacao.dto.VotoRespostaDTO;
import com.cooperativa.votacao.excecao.SessaoFechadaException;
import com.cooperativa.votacao.excecao.VotoJaRegistradoException;
import com.cooperativa.votacao.model.*;
import com.cooperativa.votacao.repository.RepositorioVoto;
import com.cooperativa.votacao.service.ServicoSessaoVotacao;
import com.cooperativa.votacao.service.ServicoVotacao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TesteServicoVotacao {

    @Mock
    RepositorioVoto repositorioVoto;

    @Mock
    ServicoSessaoVotacao servicoSessaoVotacao;

    @InjectMocks
    ServicoVotacao servicoVotacao;

    SessaoVotacao sessaoAberta;
    SessaoVotacao sessaoFechada;
    Pauta pauta;

    @BeforeEach
    void configurar() {
        pauta = new Pauta("titulo", "descricao");
        sessaoAberta = new SessaoVotacao(pauta, 5);
        sessaoFechada = new SessaoVotacao(pauta, 1);
        sessaoFechada.fechar();
    }

    @Test
    void deveRegistrarVoto() {
        VotoRequisicaoDTO requisicao = new VotoRequisicaoDTO(1L, "A1", TipoVoto.SIM);
        when(servicoSessaoVotacao.buscarEntidadeSessao(1L)).thenReturn(sessaoAberta);
        when(repositorioVoto.existsBySessaoVotacaoIdAndAssociadoId(1L, "A1")).thenReturn(false);
        when(repositorioVoto.save(any(Voto.class))).thenAnswer(i -> i.getArgument(0));

        VotoRespostaDTO resultado = servicoVotacao.registrarVoto(requisicao);

        assertThat(resultado.associadoId()).isEqualTo("A1");
        assertThat(resultado.opcaoVoto()).isEqualTo(TipoVoto.SIM);
    }

    @Test
    void deveLancarExcecaoQuandoSessaoFechada() {
        VotoRequisicaoDTO requisicao = new VotoRequisicaoDTO(2L, "A1", TipoVoto.SIM);
        when(servicoSessaoVotacao.buscarEntidadeSessao(2L)).thenReturn(sessaoFechada);

        assertThatThrownBy(() -> servicoVotacao.registrarVoto(requisicao)).isInstanceOf(SessaoFechadaException.class);
        verify(repositorioVoto, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoVotoDuplicado() {
        VotoRequisicaoDTO requisicao = new VotoRequisicaoDTO(1L, "A1", TipoVoto.SIM);
        when(servicoSessaoVotacao.buscarEntidadeSessao(1L)).thenReturn(sessaoAberta);
        when(repositorioVoto.existsBySessaoVotacaoIdAndAssociadoId(1L, "A1")).thenReturn(true);

        assertThatThrownBy(() -> servicoVotacao.registrarVoto(requisicao)).isInstanceOf(VotoJaRegistradoException.class);
    }

    @Test
    void deveContabilizarVotos() {
        when(servicoSessaoVotacao.buscarEntidadeSessao(1L)).thenReturn(sessaoAberta);
        when(repositorioVoto.countBySessaoVotacaoIdAndOpcaoVoto(1L, TipoVoto.SIM)).thenReturn(3L);
        when(repositorioVoto.countBySessaoVotacaoIdAndOpcaoVoto(1L, TipoVoto.NAO)).thenReturn(1L);

        ResultadoVotacaoDTO resultado = servicoVotacao.contabilizarVotos(1L);

        assertThat(resultado.totalVotos()).isEqualTo(4L);
        assertThat(resultado.resultado()).isEqualTo("APROVADO");
    }
}