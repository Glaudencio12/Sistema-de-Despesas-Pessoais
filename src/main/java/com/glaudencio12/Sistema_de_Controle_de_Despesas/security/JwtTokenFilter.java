package com.glaudencio12.Sistema_de_Controle_de_Despesas.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class JwtTokenFilter extends GenericFilterBean {

    private final JwtTokenProvider tokenProvider;

    public JwtTokenFilter(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    /**
     * Método executado para cada requisição HTTP que passa pelo filtro.
     * Ele extrai o token JWT do cabeçalho "Authorization", valida o token e,
     * se for válido, autentica o usuário no contexto de segurança.
     *
     * @param request  Objeto da requisição HTTP (contém os cabeçalhos e parâmetros)
     * @param response Objeto da resposta HTTP (pode ser usado para retornar erros)
     * @param filter   Cadeia de filtros do Spring Security; usado para continuar o fluxo da requisição
     * @throws IOException        Caso ocorra erro de entrada/saída durante o processo
     * @throws ServletException   Caso ocorra erro interno do servlet
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filter) throws IOException, ServletException {
        String token = tokenProvider.resolveToken((HttpServletRequest) request);

        if (token != null && tokenProvider.validateToken(token)) {
            Authentication auth = tokenProvider.getAuthentication(token);
            if (auth != null) {
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        filter.doFilter(request, response);
    }
}
