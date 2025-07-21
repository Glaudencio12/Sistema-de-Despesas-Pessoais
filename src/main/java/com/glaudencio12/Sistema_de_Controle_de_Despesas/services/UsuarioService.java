package com.glaudencio12.Sistema_de_Controle_de_Despesas.services;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.request.UsuarioRequestDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response.UsuarioResponseDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.exception.EmailCannotBeDuplicatedException;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.exception.UserNotFound;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.mapper.ObjectMapper;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.Usuario;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.repository.UsuarioRepository;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.utils.HateoasLinks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UsuarioService {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class.getName());

    private final UsuarioRepository repository;
    private final HateoasLinks linksHateoas;

    public UsuarioService(UsuarioRepository repository, HateoasLinks links) {
        this.repository = repository;
        this.linksHateoas = links;
    }

    public UsuarioResponseDTO createUser(UsuarioRequestDTO usuario) {
        logger.info("Iniciando cadastro de usuário com email: {}", usuario.getEmail());
        Usuario emailUsuarioCadastrado = repository.findByEmail(usuario.getEmail());

        if (emailUsuarioCadastrado != null) {
            throw new EmailCannotBeDuplicatedException("O email fornecido já está cadastrado na base de dados");
        } else {
            usuario.setDataCadastro(LocalDate.now());
            Usuario entidade = repository.save(ObjectMapper.parseObject(usuario, Usuario.class));
            UsuarioResponseDTO dto = ObjectMapper.parseObject(entidade, UsuarioResponseDTO.class);
            linksHateoas.links(dto);
            return dto;
        }
    }

    public UsuarioResponseDTO findUserById(Long id) {
        logger.info("Buscando usuário de ID: {}", id);
        Usuario usuario = repository.findById(id).orElseThrow(() -> new UserNotFound("Usuário não encontrado"));
        UsuarioResponseDTO dto = ObjectMapper.parseObject(usuario, UsuarioResponseDTO.class);
        linksHateoas.links(dto);
        return dto;
    }

    public List<UsuarioResponseDTO> findAllUsers() {
        logger.info("Buscando todos os usuários do banco");
        List<Usuario> usuarios = repository.findAll();
        if (usuarios.isEmpty()) {
            throw new UserNotFound("Nenhum usuário encontrada no banco de dados");
        } else {
            List<UsuarioResponseDTO> dtos = ObjectMapper.parseObjects(usuarios, UsuarioResponseDTO.class);
            dtos.forEach(linksHateoas::links);
            return dtos;
        }
    }

    public UsuarioResponseDTO updateUserById(Long id, UsuarioRequestDTO usuarioRequest) {
        logger.info("Atualizando os dados do usuário de id {}", id);
        Usuario emailUsuarioCadastrado = repository.findByEmail(usuarioRequest.getEmail());

        if (emailUsuarioCadastrado != null) {
            throw new EmailCannotBeDuplicatedException("O email fornecido já está cadastrado na base de dados");
        } else {
            Usuario usuario = repository.findById(id).orElseThrow(() -> new UserNotFound("Usuário não encontrado"));

            usuario.setNome(usuarioRequest.getNome().trim());
            usuario.setEmail(usuarioRequest.getEmail().trim());
            usuario.setSenha(usuarioRequest.getSenha().trim());
            UsuarioResponseDTO dto = ObjectMapper.parseObject(repository.save(usuario), UsuarioResponseDTO.class);
            linksHateoas.links(dto);
            return dto;
        }
    }

    public void deleteUserById(Long id) {
        logger.info("Excluindo o usuário de id {} do banco de dados", id);
        Usuario usuario = repository.findById(id).orElseThrow(() -> new UserNotFound("Usuário não encontrado"));
        repository.delete(usuario);
    }
}
