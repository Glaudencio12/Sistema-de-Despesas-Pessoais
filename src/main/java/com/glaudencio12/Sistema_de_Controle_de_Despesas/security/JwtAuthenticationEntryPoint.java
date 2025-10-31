package com.glaudencio12.Sistema_de_Controle_de_Despesas.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint{
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException{
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        response.getWriter().write(String.format(
                "{\"status\": %d, \"error\": \"Acesso não autorizado\", \"message\": \"%s\", \"path\": \"%s\"}",
                HttpServletResponse.SC_UNAUTHORIZED,
                authException.getMessage(),
                request.getRequestURI()
        ));
    }
}
