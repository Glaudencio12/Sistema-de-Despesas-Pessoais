package com.glaudencio12.Sistema_de_Controle_de_Despesas.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.loginUser.TokenDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.exception.InvalidJwtAuthenticationException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Date;

@Service
public class JwtTokenProvider {
    @Value("${security.jwt.token.secret-key:secret")
    private String chaveSecreta;

    @Value("${security.jwt.token.expire-length:3600000}")
    private Long validadeMilissegundos;

    private final UserDetailsService userDetailsService;

    private Algorithm algoritmo;

    public JwtTokenProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Inicializa o algoritmo de assinatura JWT (HMAC256) com a chave secreta.
     * Este método é executado automaticamente após a injeção de dependências.
     */
    @PostConstruct
    public void init() {
        algoritmo = Algorithm.HMAC256(chaveSecreta);
    }

    /**
     * Gera um token de acesso (JWT) com o email, papel e tempo de expiração.
     * O token é assinado com o algoritmo HMAC256 e contém claims personalizadas.
     *
     * @param email    Identificação do usuário (subject)
     * @param papel    Papel ou permissão do usuário
     * @param agora    Data de emissão do token
     * @param validade Data de expiração do token
     * @return Token JWT de acesso
     */
    public String getAcessoToken(String email, String papel, Date agora, Date validade) {
        String issueUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        return JWT.create()
                .withClaim("papel", papel)
                .withIssuedAt(agora)
                .withExpiresAt(validade)
                .withSubject(email)
                .withIssuer(issueUrl)
                .sign(algoritmo);
    }

    /**
     * Gera um token de atualização (refresh token) com validade estendida.
     * Permite renovar o token de acesso sem refazer o login.
     *
     * @param email Identificação do usuário (subject)
     * @param papel Papel ou permissão do usuário
     * @param agora Data atual (para cálculo da expiração)
     * @return Token JWT de atualização
     */
    public String refreshToken(String email, String papel, Date agora) {
        Date validadeAtualizacao = new Date(agora.getTime() + (validadeMilissegundos * 3));
        return JWT.create()
                .withClaim("papel", papel)
                .withIssuedAt(agora)
                .withExpiresAt(validadeAtualizacao)
                .withSubject(email)
                .sign(algoritmo);
    }

    /**
     * Cria um objeto TokenDTO contendo o token de acesso e o token de atualização.
     * Define os tempos de emissão e expiração e retorna ambos os tokens juntos.
     *
     * @param email Email do usuário
     * @param papel Papel ou permissão do usuário
     * @return TokenDTO com dados do token e validade
     */
    public TokenDTO createAcessToken(String email, String papel) {
        Date agora = new Date();
        Date validade = new Date(agora.getTime() + validadeMilissegundos);

        String tokenDeAcesso = getAcessoToken(email, papel, agora, validade);
        String tokenDeAtualizacao = refreshToken(email, papel, agora);

        return new TokenDTO(email, true, agora, validade, tokenDeAcesso, tokenDeAtualizacao);
    }

    /**
     * Decodifica e valida a assinatura do token JWT.
     * Caso o token seja inválido ou expirado, lança uma exceção personalizada.
     *
     * @param token Token JWT recebido na requisição
     * @return Objeto DecodedJWT com as informações decodificadas
     */
    public DecodedJWT decodedToken(String token) {
        try {
            JWTVerifier verificar = JWT.require(algoritmo).build();
            return verificar.verify(token);
        } catch (Exception e) {
            throw new InvalidJwtAuthenticationException("Token JWT expirado ou inválido");
        }
    }

    /**
     * Verifica se o token JWT ainda é válido (não expirou).
     * Também delega a verificação de integridade para decodedToken().
     *
     * @param token Token JWT a ser validado
     * @return true se o token for válido, false caso contrário
     */
    public boolean validateToken(String token) {
        try {
            DecodedJWT decodedJWT = decodedToken(token);
            return !decodedJWT.getExpiresAt().before(new Date());
        } catch (Exception e) {
            throw new InvalidJwtAuthenticationException("Token JWT expirado ou inválido");
        }
    }

    /**
     * Constrói um objeto Authentication do Spring Security com base no token JWT.
     * Carrega o usuário pelo email (subject) e associa suas permissões.
     *
     * @param token Token JWT válido
     * @return Objeto Authentication com o usuário autenticado
     */
    public Authentication getAuthentication(String token){
        DecodedJWT decodedJWT = decodedToken(token);
        UserDetails detalhesUsuario = this.userDetailsService.loadUserByUsername(decodedJWT.getSubject());
        return new UsernamePasswordAuthenticationToken(detalhesUsuario,"", detalhesUsuario.getAuthorities());
    }

    /**
     * Extrai o token JWT do cabeçalho "Authorization" da requisição HTTP.
     * Remove o prefixo "Bearer " e retorna apenas o valor do token.
     *
     * @param request Objeto HttpServletRequest da requisição
     * @return Token JWT puro ou null se não estiver presente
     */
    public String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring("Bearer ".length());
        }
        return null;
    }
}
