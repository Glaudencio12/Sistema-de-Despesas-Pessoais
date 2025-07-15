package com.glaudencio12.Sistema_de_Controle_de_Despesas.exception.execeptionResponse;

import java.util.List;

public record ExceptionResponseValidate(String timestamp, String mensagem, List<String> detalhes) { }
