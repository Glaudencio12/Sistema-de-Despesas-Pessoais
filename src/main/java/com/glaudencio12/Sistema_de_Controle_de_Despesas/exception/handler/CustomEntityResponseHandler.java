package com.glaudencio12.Sistema_de_Controle_de_Despesas.exception.handler;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.exception.EmailCannotBeDuplicatedException;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.exception.execeptionResponse.ExceptionResponse;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.exception.execeptionResponse.ExceptionResponseValidate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CustomEntityResponseHandler{

    LocalDateTime localDateTime = LocalDateTime.now();
    DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    String dataFormatada = localDateTime.format(formatoData);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<ExceptionResponseValidate> handleValidationException(MethodArgumentNotValidException ex) {
       List<String> erros = new ArrayList<>();

       for (FieldError erro : ex.getBindingResult().getFieldErrors()){
           String mensagem = erro.getField() + " : " + erro.getDefaultMessage();
           erros.add(mensagem);
       }

        ExceptionResponseValidate response = new ExceptionResponseValidate(dataFormatada, "Erro de validação", erros);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailCannotBeDuplicatedException.class)
    public final ResponseEntity<ExceptionResponse> handlerEmailDuplicatedException(Exception ex, WebRequest request){
        ExceptionResponse response = new ExceptionResponse(dataFormatada, ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
}
