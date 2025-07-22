package com.glaudencio12.Sistema_de_Controle_de_Despesas.mocks;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.request.UsuarioRequestDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response.UsuarioResponseDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.Usuario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MockUsuario {

    public Object preencherDados(Object object) {
        if (object instanceof Usuario usuario) {
            usuario.setId(1L);
            usuario.setNome("Nome test");
            usuario.setEmail("emailTest1@gmail.com");
            usuario.setSenha("senha123");
            usuario.setDataCadastro(LocalDate.now());
            return usuario;
        } else if (object instanceof UsuarioRequestDTO requestDTO) {
            requestDTO.setId(1L);
            requestDTO.setNome("Nome test");
            requestDTO.setEmail("emailTest3@gmail.com");
            requestDTO.setSenha("senha123");
            requestDTO.setDataCadastro(LocalDate.now());
            return requestDTO;
        } else if (object instanceof UsuarioResponseDTO responseDTO) {
            responseDTO.setId(1L);
            responseDTO.setNome("Nome test");
            responseDTO.setEmail("emailTest2@gmail.com");
            responseDTO.setSenha("senha123");
            responseDTO.setDataCadastro(LocalDate.now());
            return responseDTO;
        }

        throw new IllegalArgumentException("Tipo de objeto não suportado");
    }

    public List<Usuario> usuarioEntidadeList(){
        Usuario usuario = new Usuario();
        List<Usuario> usuarios = new ArrayList<>();
        for (long i = 1L; i <= 3; i++) {
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
}
