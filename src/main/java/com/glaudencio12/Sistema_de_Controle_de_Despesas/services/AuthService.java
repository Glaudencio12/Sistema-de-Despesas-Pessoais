package com.glaudencio12.Sistema_de_Controle_de_Despesas.services;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.loginUser.LoginRequestDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.loginUser.TokenDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.repository.UsuarioRepository;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.security.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UsuarioRepository usuarioRepository;

    public AuthService(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, UsuarioRepository usuarioRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Realiza a autenticação do usuário e gera tokens JWT.
     * Passos do método:
     * 1. Tenta autenticar o usuário com email e senha via AuthenticationManager.
     * 2. Se a autenticação falhar, lança BadCredentialsException.
     * 3. Busca o usuário no banco pelo email.
     * 4. Se o usuário não existir, lança UsernameNotFoundException.
     * 5. Gera token de acesso e refresh via JwtTokenProvider.
     * 6. Retorna os tokens dentro de um ResponseEntity com status 200 (OK).
     *
     * @param credenciais Objeto LoginRequestDTO contendo email e senha do usuário
     * @return ResponseEntity<TokenDTO> com token de acesso e refresh
     * @throws BadCredentialsException Se a senha estiver incorreta
     * @throws UsernameNotFoundException Se o email não existir no banco
     */
    public ResponseEntity<TokenDTO> signIn(LoginRequestDTO credenciais) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credenciais.getEmail(),
                            credenciais.getSenha()
                    )
            );
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("Usuário inexistente ou senha inválida");
        }

        var usuario = usuarioRepository.findByEmail(credenciais.getEmail());
        if (usuario == null) {
            throw new UsernameNotFoundException("Email " + credenciais.getEmail() + " não encontrado");
        }

        TokenDTO token = tokenProvider.createAcessToken(usuario.getEmail(), usuario.getPapel());
        return ResponseEntity.ok(token);
    }
}
