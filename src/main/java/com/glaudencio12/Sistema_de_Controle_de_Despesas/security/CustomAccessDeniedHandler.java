package com.glaudencio12.Sistema_de_Controle_de_Despesas.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        response.getWriter().write(String.format(
                "{\"status\": %d, \"error\": \"Acesso negado\", \"message\": \"%s\", \"path\": \"%s\"}",
                HttpServletResponse.SC_FORBIDDEN,
                accessDeniedException.getMessage(),
                request.getRequestURI()
        ));
    }
}
