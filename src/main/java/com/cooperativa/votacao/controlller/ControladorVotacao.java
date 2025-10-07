package com.cooperativa.votacao.controlller;

import com.cooperativa.votacao.dto.ResultadoVotacaoDTO;
import com.cooperativa.votacao.dto.VotoRequisicaoDTO;
import com.cooperativa.votacao.dto.VotoRespostaDTO;
import com.cooperativa.votacao.service.ServicoVotacao;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/votos")
@Tag(name = "Votação")
public class ControladorVotacao {

    private final ServicoVotacao servico;

    public ControladorVotacao(ServicoVotacao servico) {
        this.servico = servico;
    }

    @PostMapping
    @Operation(summary = "Registrar voto")
    public ResponseEntity<VotoRespostaDTO> votar(@Valid @RequestBody VotoRequisicaoDTO requisicao) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servico.registrarVoto(requisicao));
    }

    @GetMapping("/resultado/{sessaoId}")
    @Operation(summary = "Contabilizar votos")
    public ResponseEntity<ResultadoVotacaoDTO> obterResultado(@PathVariable Long sessaoId) {
        return ResponseEntity.ok(servico.contabilizarVotos(sessaoId));
    }
}