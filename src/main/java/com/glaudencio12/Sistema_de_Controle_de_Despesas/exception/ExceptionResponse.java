package com.glaudencio12.Sistema_de_Controle_de_Despesas.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ExceptionResponse(String timesTamp, String mensagem, List<String> detalhes) {
}
