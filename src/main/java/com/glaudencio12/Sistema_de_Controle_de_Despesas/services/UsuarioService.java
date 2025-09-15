package com.glaudencio12.Sistema_de_Controle_de_Despesas.services;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.request.UsuarioRequestDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response.UsuarioResponseDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.exception.EmailCannotBeDuplicatedException;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.exception.NotFoundElementException;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.mapper.ObjectMapper;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.Usuario;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.repository.UsuarioRepository;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.utils.HateoasLinks;
import org.apache.el.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class UsuarioService {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class.getName());

    private final UsuarioRepository repository;
    private final HateoasLinks linksHateoas;

    public UsuarioService(UsuarioRepository repository, HateoasLinks links) {
        this.repository = repository;
        this.linksHateoas = links;
    }

    public UsuarioResponseDTO createUser(UsuarioRequestDTO usuarioRequest) {
        logger.info("Iniciando cadastro de usuário com email: {}", usuarioRequest.getEmail());
        Usuario usuarioCadastrado = repository.findByEmail(usuarioRequest.getEmail());

        if (usuarioCadastrado != null) {
            throw new EmailCannotBeDuplicatedException("O email fornecido já está cadastrado na base de dados");
        } else {
            usuarioRequest.setDataCadastro(LocalDate.now());
            Usuario entidade = repository.save(ObjectMapper.parseObject(usuarioRequest, Usuario.class));
            UsuarioResponseDTO dto = ObjectMapper.parseObject(entidade, UsuarioResponseDTO.class);
            linksHateoas.links(dto);
            return dto;
        }
    }

    public UsuarioResponseDTO findUserById(Long id) {
        logger.info("Buscando usuário de ID: {}", id);
        Usuario usuario = repository.findById(id).orElseThrow(() -> new NotFoundElementException("Usuário não encontrado"));
        UsuarioResponseDTO dto = ObjectMapper.parseObject(usuario, UsuarioResponseDTO.class);
        linksHateoas.links(dto);
        return dto;
    }

    public List<UsuarioResponseDTO> findAllUsers() {
        logger.info("Buscando todos os usuários do banco");
        List<Usuario> usuarios = repository.findAll();
        if (usuarios.isEmpty()) {
            throw new NotFoundElementException("Nenhum usuário encontrada no banco de dados");
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
            Usuario usuario = repository.findById(id).orElseThrow(() -> new NotFoundElementException("Usuário não encontrado"));

            usuario.setNome(usuarioRequest.getNome().trim());
            usuario.setEmail(usuarioRequest.getEmail().trim());
            usuario.setSenha(usuarioRequest.getSenha().trim());
            UsuarioResponseDTO dto = ObjectMapper.parseObject(repository.save(usuario), UsuarioResponseDTO.class);
            linksHateoas.links(dto);
            return dto;
        }
    }

    public UsuarioResponseDTO updateSpecificFields(Long id, Map<String, Object> campos) {
        logger.info("Atualizando o campo {} do usuário", campos.keySet());
        Usuario usuarioCadastrado = repository.findById(id).orElseThrow(() -> new NotFoundElementException("Usuário não encontrado"));

        Set<String> camposPermitidos = Set.of("nome","email", "senha");

        for (String chave : campos.keySet()) {
            if (!camposPermitidos.contains(chave)) {
                throw new IllegalArgumentException("Campo não pode ser atualizado, pois somente os seguintes campos podem: " + camposPermitidos);
            }
        }

        campos.forEach((campo, valor) -> {
            Field field = ReflectionUtils.findField(Usuario.class, campo);
            if (!(valor instanceof String) || ((String) valor).isEmpty()) {
                throw new IllegalArgumentException("Campo " + field.getName() + " não pode ser atualizado, pois precisa ser do tipo String e não pode ser nulo");
            }
            field.setAccessible(true);
            ReflectionUtils.setField(field, usuarioCadastrado, valor);
        });

        UsuarioResponseDTO dto = ObjectMapper.parseObject(repository.save(usuarioCadastrado), UsuarioResponseDTO.class);
        linksHateoas.links(dto);
        return dto;
    }

    public void deleteUserById(Long id) {
        logger.info("Excluindo o usuário de id {} do banco de dados", id);
        Usuario usuario = repository.findById(id).orElseThrow(() -> new NotFoundElementException("Usuário não encontrado"));
        repository.delete(usuario);
    }
}
