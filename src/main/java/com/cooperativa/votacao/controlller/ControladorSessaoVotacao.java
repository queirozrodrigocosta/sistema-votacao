package com.cooperativa.votacao.controlller;

import com.cooperativa.votacao.dto.SessaoVotacaoRequisicaoDTO;
import com.cooperativa.votacao.dto.SessaoVotacaoRespostaDTO;
import com.cooperativa.votacao.service.ServicoSessaoVotacao;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sessoes")
@Tag(name = "Sessões de Votação")
public class ControladorSessaoVotacao {

    private final ServicoSessaoVotacao servico;

    public ControladorSessaoVotacao(ServicoSessaoVotacao servico) {
        this.servico = servico;
    }

    @PostMapping
    @Operation(summary = "Abrir sessão de votação")
    public ResponseEntity<SessaoVotacaoRespostaDTO> abrir(@Valid @RequestBody SessaoVotacaoRequisicaoDTO requisicao) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servico.abrirSessao(requisicao));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar sessão por ID")
    public ResponseEntity<SessaoVotacaoRespostaDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(servico.buscarSessao(id));
    }

    @GetMapping
    @Operation(summary = "Listar sessões")
    public ResponseEntity<List<SessaoVotacaoRespostaDTO>> listar(@RequestParam(required = false) Long pautaId) {
        List<SessaoVotacaoRespostaDTO> resultado = (pautaId == null)
            ? servico.listarSessoes()
            : servico.listarSessoesPorPauta(pautaId);
        return ResponseEntity.ok(resultado);
    }
}