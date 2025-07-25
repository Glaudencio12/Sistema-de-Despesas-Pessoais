package com.glaudencio12.Sistema_de_Controle_de_Despesas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CategoryCannotBeDuplicateException extends RuntimeException {
    public CategoryCannotBeDuplicateException(String message) {
        super(message);
    }
}
