package com.cooperativa.votacao.controlller;

import com.cooperativa.votacao.dto.PautaRequisicaoDTO;
import com.cooperativa.votacao.dto.PautaRespostaDTO;
import com.cooperativa.votacao.service.ServicoPauta;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pautas")
@Tag(name = "Pautas")
public class ControladorPauta {

    private final ServicoPauta servicoPauta;

    public ControladorPauta(ServicoPauta servicoPauta) {
        this.servicoPauta = servicoPauta;
    }

    @PostMapping
    @Operation(summary = "Criar nova pauta")
    public ResponseEntity<PautaRespostaDTO> criar(@Valid @RequestBody PautaRequisicaoDTO requisicao) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicoPauta.criarPauta(requisicao));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pauta por ID")
    public ResponseEntity<PautaRespostaDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(servicoPauta.buscarPauta(id));
    }

    @GetMapping
    @Operation(summary = "Listar pautas")
    public ResponseEntity<List<PautaRespostaDTO>> listar(@RequestParam(required = false) String titulo) {
        List<PautaRespostaDTO> resultado = (titulo == null || titulo.isBlank())
            ? servicoPauta.listarPautas()
            : servicoPauta.buscarPautasPorTitulo(titulo);
        return ResponseEntity.ok(resultado);
    }
}