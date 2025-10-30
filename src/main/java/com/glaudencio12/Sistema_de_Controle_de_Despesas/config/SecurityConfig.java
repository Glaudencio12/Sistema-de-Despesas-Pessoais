package com.glaudencio12.Sistema_de_Controle_de_Despesas.config;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.security.JwtTokenFilter;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtTokenProvider tokenProvider;
    public static final String SECURITY = "bearerAuth";

    public SecurityConfig(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    /**
     * Cria um bean do AuthenticationManager.
     * É usado para realizar a autenticação de usuários, geralmente no login.
     *
     * @param configuration Configuração de autenticação do Spring
     * @return AuthenticationManager configurado com UserDetailsService e PasswordEncoder
     * @throws Exception Caso ocorra algum problema na criação do AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * Configura a cadeia de filtros de segurança (SecurityFilterChain).
     * Define quais endpoints são públicos, quais exigem autenticação, políticas de sessão,
     * e adiciona o filtro JWT para validar tokens em todas as requisições.
     *
     * @param httpSecurity Configuração HTTP do Spring Security
     * @return SecurityFilterChain configurado para a aplicação
     * @throws Exception Caso ocorra erro na configuração do HttpSecurity
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        JwtTokenFilter filter = new JwtTokenFilter(tokenProvider);

        return httpSecurity
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(
                        authorizationHttpRequest -> authorizationHttpRequest
                                .requestMatchers(
                                        "/api/auth/login",
                                        "/api/auth/refresh/**",
                                        "/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "/v3/api-docs/**",
                                        "/api/usuarios/createUser"
                                ).permitAll()
                                .requestMatchers("/api/lancamentos", "/api/lancamentos/**").authenticated()
                                .requestMatchers("/api/usuarios", "/api/usuarios/**").authenticated()
                                .requestMatchers("/api/categorias", "/api/categorias/**").authenticated()
                                .requestMatchers("/users").denyAll()
                )
                .cors(Customizer.withDefaults())
                .build();
    }
}
