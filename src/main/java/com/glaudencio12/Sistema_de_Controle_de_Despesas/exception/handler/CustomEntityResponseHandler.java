package com.glaudencio12.Sistema_de_Controle_de_Despesas.exception.handler;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.exception.CategoryCannotBeDuplicateException;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.exception.EmailCannotBeDuplicatedException;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.exception.InvalidTokenException;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.exception.NotFoundElementException;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.exception.execeptionResponse.ExceptionResponse;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.exception.execeptionResponse.ExceptionResponseValidate;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.utils.DataFormatada;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import org.springframework.security.access.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CustomEntityResponseHandler{

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<ExceptionResponseValidate> handleValidationException(MethodArgumentNotValidException ex) {
       List<String> erros = new ArrayList<>();

       for (FieldError erro : ex.getBindingResult().getFieldErrors()){
           String mensagem = erro.getField() + " : " + erro.getDefaultMessage();
           erros.add(mensagem);
       }

        ExceptionResponseValidate response = new ExceptionResponseValidate(DataFormatada.data(), "Erro de validação", erros);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({EmailCannotBeDuplicatedException.class, CategoryCannotBeDuplicateException.class})
    public final ResponseEntity<ExceptionResponse> handlerEmailOrCategoryDuplicatedException(Exception ex, WebRequest request){
        ExceptionResponse response = new ExceptionResponse(DataFormatada.data(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotFoundElementException.class)
    public final ResponseEntity<ExceptionResponse> handlerUserNotFound(Exception ex, WebRequest request){
        ExceptionResponse response = new ExceptionResponse(DataFormatada.data(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<ExceptionResponse> handlerIllegalArgument(Exception e, WebRequest request){
        ExceptionResponse response = new ExceptionResponse(DataFormatada.data(), e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public final ResponseEntity<ExceptionResponse> handlerTokenInvalid(Exception e, WebRequest request){
        ExceptionResponse response = new ExceptionResponse(DataFormatada.data(), e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<ExceptionResponse> handlerPermission(WebRequest request){
        ExceptionResponse response = new ExceptionResponse(
                DataFormatada.data(),
                "Acesso negado: você não tem permissão para acessar este recurso.",
                request.getDescription(false)
        );
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
}
