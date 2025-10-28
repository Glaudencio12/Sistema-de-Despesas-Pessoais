package com.glaudencio12.Sistema_de_Controle_de_Despesas.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.loginUser.TokenDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.exception.InvalidTokenException; // Usando a exceção 401
import io.micrometer.common.util.StringUtils;
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
     */
    @PostConstruct
    public void init() {
        algoritmo = Algorithm.HMAC256(chaveSecreta);
    }

    /**
     * Gera um token de acesso (JWT) com o email, papel e tempo de expiração.
     */
    public String getAccessToken(String email, String papel, Date agora, Date validade) {
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
     */
    public String getRefreshToken(String email, String papel, Date agora) {
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
     */
    public TokenDTO createAccessToken(String email, String papel) {
        Date agora = new Date();
        Date validade = new Date(agora.getTime() + validadeMilissegundos);

        String tokenDeAcesso = getAccessToken(email, papel, agora, validade);
        String tokenDeAtualizacao = getRefreshToken(email, papel, agora);

        return new TokenDTO(email, true, agora, validade, tokenDeAcesso, tokenDeAtualizacao);
    }


    /**
     * Cria um objeto TokenDTO contendo um novo token de acesso e um novo token de atualização.
     *
     * @param refreshToken token de atualização fornecido na autenticação
     * @return TokenDTO com dados do token e validade
     */
    public TokenDTO refreshToken(String refreshToken) {
        String token;
        if (StringUtils.isNotBlank(refreshToken) && refreshToken.startsWith("Bearer")) {
            token = refreshToken.substring("Bearer ".length());
        } else {
            throw new InvalidTokenException("Refresh Token mal formado ou faltando o prefixo 'Bearer '.");
        }

        try {
            DecodedJWT decodedJWT = decodedToken(token);
            String email = decodedJWT.getSubject();
            String papel = decodedJWT.getClaim("papel").asString();
            return createAccessToken(email, papel);
        } catch (Exception e) {
            throw new InvalidTokenException("Erro interno ao processar Refresh Token.");
        }
    }

    /**
     * Decodifica e valida a assinatura do token JWT.
     * Caso o token seja inválido ou expirado, lança a exceção 401.
     *
     * @param token Token JWT recebido na requisição
     * @return Objeto DecodedJWT com as informações decodificadas
     * @throws InvalidTokenException Se o token estiver expirado ou inválido (401)
     */
    public DecodedJWT decodedToken(String token) {
        try {
            JWTVerifier verificar = JWT.require(algoritmo).build();
            return verificar.verify(token);
        } catch (TokenExpiredException e) {
            throw new InvalidTokenException("Token expirado. Solicite um novo login ou use o Refresh Token.");
        } catch (JWTVerificationException e) {
            throw new InvalidTokenException("Token inválido ou malformado. Verificação falhou.");
        }
    }

    /**
     * Verifica se o token JWT ainda é válido (não expirou).
     *
     * @param token Token JWT a ser validado
     * @return true se o token for válido, false caso contrário
     * @throws InvalidTokenException Se o token estiver expirado ou inválido (401)
     */
    public boolean validateToken(String token) {
        try {
            DecodedJWT decodedJWT = decodedToken(token);
            return !decodedJWT.getExpiresAt().before(new Date());
        } catch (InvalidTokenException e) {
            throw new InvalidTokenException("Token expirado. Solicite um novo login ou use o Refresh Token.");
        }
    }

    /**
     * Constrói um objeto Authentication do Spring Security com base no token JWT.
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
