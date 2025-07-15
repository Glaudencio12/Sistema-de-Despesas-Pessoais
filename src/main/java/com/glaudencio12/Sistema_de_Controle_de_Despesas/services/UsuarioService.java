package com.glaudencio12.Sistema_de_Controle_de_Despesas.services;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.request.UsuarioRequestDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response.UsuarioResponseDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.exception.EmailCannotBeDuplicatedException;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.mapper.ObjectMapper;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.Usuario;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UsuarioService {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class.getName());

    @Autowired
    private UsuarioRepository repository;

    public UsuarioResponseDTO createUser(UsuarioRequestDTO usuario){
        logger.info("Iniciando cadastro de usuário com email: {}", usuario.getEmail());
        Usuario usuariCadastrado = repository.findByEmail(usuario.getEmail());
        if (usuariCadastrado != null) {
            throw new EmailCannotBeDuplicatedException("O email fornecido já está cadastrado na base de dados");
        }else {
            usuario.setDataCadastro(LocalDate.now());
            Usuario entidade = repository.save(ObjectMapper.parseObject(usuario, Usuario.class));
            return ObjectMapper.parseObject(entidade, UsuarioResponseDTO.class);
        }
    }
}
