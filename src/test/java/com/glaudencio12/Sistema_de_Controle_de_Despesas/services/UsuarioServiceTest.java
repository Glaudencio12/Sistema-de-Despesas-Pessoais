package com.glaudencio12.Sistema_de_Controle_de_Despesas.services;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.request.UsuarioRequestDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response.UsuarioResponseDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.exception.EmailCannotBeDuplicatedException;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.exception.NotFoundElementException;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.stubs.StubsUsuario;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.Usuario;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.repository.UsuarioRepository;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.utils.HateoasLinks;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class UsuarioServiceTest {

    @Mock
    UsuarioRepository repository;

    @Mock
    HateoasLinks hateoasLinks;

    @Mock
    ModelMapper modelMapper;

    @Mock
    PagedResourcesAssembler<UsuarioResponseDTO> assembler;

    @InjectMocks
    UsuarioService service;

    Usuario usuarioEntidade;
    UsuarioRequestDTO usuarioRequestMock;
    UsuarioResponseDTO usuarioResponseMock;
    List<Usuario> usuarioEntidadeList;

    @BeforeEach
    void setUp() {
        usuarioRequestMock = StubsUsuario.usuarioRequest();
        usuarioResponseMock = StubsUsuario.usuarioResponse();
        usuarioEntidade = StubsUsuario.usuarioEntidade();
        usuarioEntidadeList = StubsUsuario.usuarioEntidadeList();
    }

    public static void links(UsuarioResponseDTO dto, String rel, String href, String type) {
        assertTrue(dto.getLinks().stream().anyMatch(link ->
                link.getRel().value().equals(rel) &
                link.getHref().endsWith(href) &
                link.getType().equals(type)
        ));
    }

    @Test
    @DisplayName("Simula o cadastro de um usuário no banco")
    void cria_um_usuario_no_banco() {
        when(repository.findByEmail(usuarioRequestMock.getEmail())).thenReturn(null);
        when(repository.save(any(Usuario.class))).thenReturn(usuarioEntidade);
        when(modelMapper.map(any(UsuarioRequestDTO.class), eq(Usuario.class))).thenReturn(usuarioEntidade);
        when(modelMapper.map(any(Usuario.class), eq(UsuarioResponseDTO.class))).thenReturn(usuarioResponseMock);
        doCallRealMethod().when(hateoasLinks).links(any(UsuarioResponseDTO.class));

        UsuarioResponseDTO resposta = service.createUser(usuarioRequestMock);

        assertNotNull(resposta);
        assertNotNull(resposta.getLinks());
        verify(repository, atLeastOnce()).findByEmail(usuarioRequestMock.getEmail());
        verify(repository, atLeastOnce()).save(any(Usuario.class));
        verify(hateoasLinks, times(1)).links(any(UsuarioResponseDTO.class));
        verify(modelMapper, times(1)).map(any(UsuarioRequestDTO.class), eq(Usuario.class));
        verify(modelMapper, times(1)).map(any(Usuario.class), eq(UsuarioResponseDTO.class));

    }

    @Test
    @DisplayName("Busca um usuário por ID")
    void busca_um_usuario_no_banco_pelo_id() {
        when(repository.findById(usuarioRequestMock.getId())).thenReturn(Optional.of(usuarioEntidade));
        when(modelMapper.map(any(Usuario.class), eq(UsuarioResponseDTO.class))).thenReturn(usuarioResponseMock);
        doCallRealMethod().when(hateoasLinks).links(any(UsuarioResponseDTO.class));

        UsuarioResponseDTO resposta = service.findUserById(usuarioRequestMock.getId());

        assertNotNull(resposta);
        assertNotNull(resposta.getLinks());
        assertNotNull(resposta.getId());
        verify(repository, atLeastOnce()).findById(usuarioRequestMock.getId());
        verify(hateoasLinks, times(1)).links(any(UsuarioResponseDTO.class));
        verify(modelMapper, times(1)).map(any(Usuario.class), eq(UsuarioResponseDTO.class));


        links(resposta, "FindUserById", "/api/usuarios/1", "GET");
        links(resposta, "FindAllUsers", "/api/usuarios", "GET");
        links(resposta, "CreateUser", "/api/usuarios", "POST");
        links(resposta, "UpdateUserById", "/api/usuarios/1", "PUT");
        links(resposta, "DeleteUser", "/api/usuarios/1", "DELETE");
    }

    @Test
    @DisplayName("Busca todos os usuários cadastrados no banco")
    void busca_todos_os_usuarios_cadastrados() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Usuario> page = new PageImpl<>(usuarioEntidadeList);

        when(repository.findAll(pageable)).thenReturn(page);
        when(modelMapper.map(any(Usuario.class), eq(UsuarioResponseDTO.class))).thenReturn(usuarioResponseMock);
        doNothing().when(hateoasLinks).links(any(UsuarioResponseDTO.class));

        List<UsuarioResponseDTO> usuarioDTO = page.stream().map(usuario -> modelMapper.map(usuario, UsuarioResponseDTO.class)).toList();

        PagedModel<EntityModel<UsuarioResponseDTO>> pagedModel =
                PagedModel.of(
                        usuarioDTO.stream().map(EntityModel::of).toList(),
                        new PagedModel.PageMetadata(10, 0, usuarioDTO.size())
                );

        when(assembler.toModel(any(Page.class))).thenReturn(pagedModel);

        PagedModel<EntityModel<UsuarioResponseDTO>> respostaPagedModel = service.findAllUsers(pageable);

        assertNotNull(respostaPagedModel);
        assertEquals(10, respostaPagedModel.getContent().size());
        verify(repository).findAll(pageable);
        verify(modelMapper, times(20)).map(any(Usuario.class), eq(UsuarioResponseDTO.class));
        verify(hateoasLinks, times(10)).links(any(UsuarioResponseDTO.class));
        verify(assembler).toModel(any(Page.class));
    }

    @Test
    @DisplayName("Atualiza os dados do usuário especificado pelo ID")
    void atualiza_os_dados_do_usurio() {
        when(repository.findByEmail(usuarioRequestMock.getEmail())).thenReturn(null);
        when(repository.findById(usuarioRequestMock.getId())).thenReturn(Optional.of(usuarioEntidade));
        when(repository.save(any(Usuario.class))).thenReturn(usuarioEntidade);
        when(modelMapper.map(any(Usuario.class), eq(UsuarioResponseDTO.class))).thenReturn(usuarioResponseMock);
        doCallRealMethod().when(hateoasLinks).links(any(UsuarioResponseDTO.class));

        UsuarioResponseDTO resposta = service.updateUserById(usuarioRequestMock.getId(), usuarioRequestMock);

        assertNotNull(resposta);
        assertNotNull(resposta.getLinks());
        verify(repository, atLeastOnce()).findByEmail(usuarioRequestMock.getEmail());
        verify(repository, atLeastOnce()).findById(usuarioRequestMock.getId());
        verify(repository, atLeastOnce()).save(any(Usuario.class));
        verify(hateoasLinks, times(1)).links(any(UsuarioResponseDTO.class));
        verify(modelMapper, times(1)).map(any(Usuario.class), eq(UsuarioResponseDTO.class));

        links(resposta, "FindUserById", "/api/usuarios/1", "GET");
        links(resposta, "FindAllUsers", "/api/usuarios", "GET");
        links(resposta, "CreateUser", "/api/usuarios", "POST");
        links(resposta, "UpdateUserById", "/api/usuarios/1", "PUT");
        links(resposta, "DeleteUser", "/api/usuarios/1", "DELETE");
    }

    @Test
    @DisplayName("Deleta o usuário especificado pelo ID")
    void deleta_um_usuario_do_banco_de_dados() {
        when(repository.findById(usuarioRequestMock.getId())).thenReturn(Optional.of(usuarioEntidade));
        doNothing().when(repository).delete(usuarioEntidade);

        service.deleteUserById(usuarioRequestMock.getId());

        verify(repository, atLeastOnce()).findById(usuarioRequestMock.getId());
        verify(repository, times(1)).delete(any(Usuario.class));
    }

    @Nested
    @DisplayName("Retorna uma exceção UseNotFoundExcepion e EmailCannotBeDuplicateException")
    class ExceptionsUser {

        @Test
        @DisplayName("Lança uma exceção EmailCannotBeDuplicateException se o email já estiver cadastrado no banco")
        void lanca_excecao_se_o_email_estiver_duplicado() {
            when(repository.findByEmail(usuarioRequestMock.getEmail())).thenReturn(usuarioEntidade);

            Exception ex1 = assertThrows(EmailCannotBeDuplicatedException.class, () -> {
                service.createUser(usuarioRequestMock);
            });

            assertEquals("O email fornecido já está cadastrado na base de dados", ex1.getMessage());
            verify(repository, atLeastOnce()).findByEmail(usuarioRequestMock.getEmail());

            Exception ex2 = assertThrows(EmailCannotBeDuplicatedException.class, () -> {
                service.updateUserById(usuarioRequestMock.getId(), usuarioRequestMock);
            });

            assertEquals("O email fornecido já está cadastrado na base de dados", ex2.getMessage());
            verify(repository, atLeastOnce()).findByEmail(usuarioRequestMock.getEmail());
        }

        @Test
        @DisplayName("Lança uma exceção UserNotFound se nenhum usuário for encontrado na busca")
        void lanca_uma_excecao_se_nenhum_usuario_for_encontrado() {
            Pageable pageble = PageRequest.of(0, 10);
            Page<Usuario> page = Page.empty();

            when(repository.findAll(pageble)).thenReturn(page);

            Exception ex = assertThrows(NotFoundElementException.class, () ->
                    service.findAllUsers(pageble)
            );

            assertEquals("Nenhum usuário encontrada no banco de dados", ex.getMessage());
            verify(repository, atLeastOnce()).findAll(pageble);
        }

        @Test
        @DisplayName("Lança uma exceção UserNotFound se o usuário especificado por ID não for encontrado")
        void lanca_uma_excecao_se_o_usuario_nao_for_encontrado() {
            when(repository.findById(usuarioRequestMock.getId())).thenReturn(Optional.empty());

            Exception ex1 = assertThrows(NotFoundElementException.class, () ->
                    service.findUserById(usuarioRequestMock.getId())
            );

            assertEquals("Usuário não encontrado", ex1.getMessage());

            Exception ex2 = assertThrows(NotFoundElementException.class, () ->
                    service.updateUserById(usuarioRequestMock.getId(), usuarioRequestMock)
            );

            assertEquals("Usuário não encontrado", ex2.getMessage());

            Exception ex3 = assertThrows(NotFoundElementException.class, () ->
                    service.deleteUserById(usuarioRequestMock.getId())
            );

            assertEquals("Usuário não encontrado", ex3.getMessage());

            verify(repository, times(3)).findById(usuarioRequestMock.getId());
        }
    }
}