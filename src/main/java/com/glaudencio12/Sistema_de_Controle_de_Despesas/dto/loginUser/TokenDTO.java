package com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.loginUser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenDTO {
    private String email;
    private Boolean autenticado;
    private Date criado;
    private Date expiracao;
    private String tokenDeAcesso;
    private String tokenDeAtualizacao;
}
