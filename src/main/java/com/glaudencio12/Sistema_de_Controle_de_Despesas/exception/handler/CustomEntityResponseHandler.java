package com.glaudencio12.Sistema_de_Controle_de_Despesas.exception.handler;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.exception.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@ControllerAdvice
public class CustomEntityResponseHandler{
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<ExceptionResponse> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        List<String> erros = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + " : " + err.getDefaultMessage())
                .toList();

        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String dataFormatada = localDateTime.format(formatoData);

        ExceptionResponse response = new ExceptionResponse(dataFormatada, "Erro de validação", erros);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
