package com.glaudencio12.Sistema_de_Controle_de_Despesas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EmailCannotBeDuplicatedException extends RuntimeException {
    public EmailCannotBeDuplicatedException(String message) {
        super(message);
    }
}
