package com.glaudencio12.Sistema_de_Controle_de_Despesas.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataFormatada {

    public static String data(){
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd-MM-yyyy | HH:mm:ss");
        return localDateTime.format(formatoData);
    }
}
