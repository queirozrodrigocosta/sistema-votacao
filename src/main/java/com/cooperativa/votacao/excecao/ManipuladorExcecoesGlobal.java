package com.cooperativa.votacao.excecao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ManipuladorExcecoesGlobal {

    private static final Logger logger = LoggerFactory.getLogger(ManipuladorExcecoesGlobal.class);

    public record RespostaErro(int status, String mensagem, LocalDateTime dataHora) {}
    public record RespostaErroValidacao(int status, String mensagem, LocalDateTime dataHora, Map<String, String> erros) {}

    @ExceptionHandler(PautaNaoEncontradaException.class)
    public ResponseEntity<RespostaErro> manipularPautaNaoEncontrada(PautaNaoEncontradaException ex) {
        logger.warn("Pauta não encontrada: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new RespostaErro(HttpStatus.NOT_FOUND.value(), ex.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(SessaoNaoEncontradaException.class)
    public ResponseEntity<RespostaErro> manipularSessaoNaoEncontrada(SessaoNaoEncontradaException ex) {
        logger.warn("Sessão não encontrada: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new RespostaErro(HttpStatus.NOT_FOUND.value(), ex.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(ExcecaoNegocio.class)
    public ResponseEntity<RespostaErro> manipularExcecaoNegocio(ExcecaoNegocio ex) {
        logger.warn("Erro de negócio: {}", ex.getMessage());
        return ResponseEntity.badRequest()
            .body(new RespostaErro(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RespostaErroValidacao> manipularValidacao(MethodArgumentNotValidException ex) {
        Map<String, String> erros = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(erro -> {
            String campo = ((FieldError) erro).getField();
            erros.put(campo, erro.getDefaultMessage());
        });
        logger.warn("Erro de validação: {}", erros);
        return ResponseEntity.badRequest()
            .body(new RespostaErroValidacao(HttpStatus.BAD_REQUEST.value(), "Dados inválidos", LocalDateTime.now(), erros));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RespostaErro> manipularErroGenerico(Exception ex) {
        logger.error("Erro interno do servidor", ex);
        return ResponseEntity.internalServerError()
            .body(new RespostaErro(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno do servidor", LocalDateTime.now()));
    }
}