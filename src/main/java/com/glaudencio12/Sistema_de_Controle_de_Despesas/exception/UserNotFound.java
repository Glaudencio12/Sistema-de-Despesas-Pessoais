package com.glaudencio12.Sistema_de_Controle_de_Despesas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFound extends RuntimeException {
    public UserNotFound(String message) {
        super(message);
    }
}
