package com.glaudencio12.Sistema_de_Controle_de_Despesas.stubs;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.request.UsuarioRequestDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response.UsuarioResponseDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.Usuario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StubsUsuario {

    public static List<Usuario> usuarioEntidadeList() {
        Usuario usuario = new Usuario();
        List<Usuario> usuarios = new ArrayList<>();

        for (long i = 1L; i <= 10; i++) {
            usuario.setId(i);
            usuario.setNome("Nomes test " + i);
            usuario.setEmail("email@gmail.com " + i);
            usuario.setSenha("senha123 " + i);
            usuario.setDataCadastro(LocalDate.now());

            usuarios.add(usuario);
            usuario = new Usuario();
        }
        return usuarios;
    }

    public static Usuario usuarioEntidade() {
        Usuario usuario = new Usuario();

        usuario.setId(1L);
        usuario.setNome("Nomes test");
        usuario.setEmail("email@gmail.com");
        usuario.setSenha("senha123");
        usuario.setDataCadastro(LocalDate.now());
        return usuario;
    }

    public static UsuarioRequestDTO usuarioRequest(){
        UsuarioRequestDTO requestDTO = new UsuarioRequestDTO();

        requestDTO.setId(1L);
        requestDTO.setNome("Nomes test");
        requestDTO.setEmail("email@gmail.com");
        requestDTO.setSenha("senha123");
        requestDTO.setDataCadastro(LocalDate.now());
        return requestDTO;
    }

    public static UsuarioResponseDTO usuarioResponse(){
        UsuarioResponseDTO responseDTO = new UsuarioResponseDTO();

        responseDTO.setId(1L);
        responseDTO.setNome("Nomes test");
        responseDTO.setEmail("email@gmail.com");
        responseDTO.setDataCadastro(LocalDate.now());
        return responseDTO;
    }
}
