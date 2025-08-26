package com.glaudencio12.Sistema_de_Controle_de_Despesas.services;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.request.UsuarioRequestDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response.UsuarioResponseDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.exception.EmailCannotBeDuplicatedException;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.exception.NotFoundException;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.mocks.MockUsuario;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.Usuario;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.repository.UsuarioRepository;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.utils.HateoasLinks;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @InjectMocks
    UsuarioService service;

    MockUsuario mockUsuario;
    Usuario usuarioEntidade;
    UsuarioRequestDTO usuarioRequestMock;
    List<Usuario> usuarioEntidadeList;

    @BeforeEach
    void setUp() {
        mockUsuario = new MockUsuario();

        usuarioRequestMock = (UsuarioRequestDTO) mockUsuario.preencherDados(new UsuarioRequestDTO());
        usuarioEntidade = (Usuario) mockUsuario.preencherDados(new Usuario());
        usuarioEntidadeList = mockUsuario.usuarioEntidadeList();
    }

    public static void links(UsuarioResponseDTO dto, String rel, String href, String type){
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
        doCallRealMethod().when(hateoasLinks).links(any(UsuarioResponseDTO.class));

        UsuarioResponseDTO resposta = service.createUser(usuarioRequestMock);

        assertNotNull(resposta);
        assertNotNull(resposta.getLinks());
        verify(repository, atLeastOnce()).findByEmail(usuarioRequestMock.getEmail());
        verify(repository, atLeastOnce()).save(any(Usuario.class));
        verify(hateoasLinks, times(1)).links(any(UsuarioResponseDTO.class));

        links(resposta, "FindUserById", "/api/usuarios/1", "GET");
        links(resposta, "FindAllUsers", "/api/usuarios", "GET");
        links(resposta, "CreateUser", "/api/usuarios", "POST");
        links(resposta, "UpdateUserById", "/api/usuarios/1", "PUT");
        links(resposta, "DeleteUser", "/api/usuarios/1", "DELETE");
    }

    @Test
    @DisplayName("Busca um usuário por ID")
    void busca_um_usuario_no_banco_pelo_id() {
        when(repository.findById(usuarioRequestMock.getId())).thenReturn(Optional.of(usuarioEntidade));
        doCallRealMethod().when(hateoasLinks).links(any(UsuarioResponseDTO.class));

        UsuarioResponseDTO resposta = service.findUserById(usuarioRequestMock.getId());

        assertNotNull(resposta);
        assertNotNull(resposta.getLinks());
        assertNotNull(resposta.getId());
        verify(repository, atLeastOnce()).findById(usuarioRequestMock.getId());
        verify(hateoasLinks, times(1)).links(any(UsuarioResponseDTO.class));

        links(resposta, "FindUserById", "/api/usuarios/1", "GET");
        links(resposta, "FindAllUsers", "/api/usuarios", "GET");
        links(resposta, "CreateUser", "/api/usuarios", "POST");
        links(resposta, "UpdateUserById", "/api/usuarios/1", "PUT");
        links(resposta, "DeleteUser", "/api/usuarios/1", "DELETE");
    }

    @Test
    @DisplayName("Busca todos os usuários cadastrados no banco")
    void busca_todos_os_usuarios_cadastrados() {
        when(repository.findAll()).thenReturn(usuarioEntidadeList);
        doCallRealMethod().when(hateoasLinks).links(any(UsuarioResponseDTO.class));

        List<UsuarioResponseDTO> resposta = service.findAllUsers();

        assertNotNull(resposta);
        assertEquals(3, resposta.size());
        verify(repository, atLeastOnce()).findAll();
        verify(hateoasLinks, times(3)).links(any(UsuarioResponseDTO.class));

        UsuarioResponseDTO usuario1 = resposta.getFirst();
        links(usuario1, "FindUserById", "/api/usuarios/1", "GET");
        links(usuario1, "FindAllUsers", "/api/usuarios", "GET");
        links(usuario1, "CreateUser", "/api/usuarios", "POST");
        links(usuario1, "UpdateUserById", "/api/usuarios/1", "PUT");
        links(usuario1, "DeleteUser", "/api/usuarios/1", "DELETE");

        UsuarioResponseDTO usuario2 = resposta.getLast();
        links(usuario2, "FindUserById", "/api/usuarios/3", "GET");
        links(usuario2, "FindAllUsers", "/api/usuarios", "GET");
        links(usuario2, "CreateUser", "/api/usuarios", "POST");
        links(usuario2, "UpdateUserById", "/api/usuarios/3", "PUT");
        links(usuario2, "DeleteUser", "/api/usuarios/3", "DELETE");
    }

    @Test
    @DisplayName("Atualiza os dados do usuário especificado pelo ID")
    void atualiza_os_dados_do_usurio() {
        when(repository.findByEmail(usuarioRequestMock.getEmail())).thenReturn(null);
        when(repository.findById(usuarioRequestMock.getId())).thenReturn(Optional.of(usuarioEntidade));
        when(repository.save(any(Usuario.class))).thenReturn(usuarioEntidade);
        doCallRealMethod().when(hateoasLinks).links(any(UsuarioResponseDTO.class));

        UsuarioResponseDTO resposta = service.updateUserById(usuarioRequestMock.getId(), usuarioRequestMock);

        assertNotNull(resposta);
        assertNotNull(resposta.getLinks());
        verify(repository, atLeastOnce()).findByEmail(usuarioRequestMock.getEmail());
        verify(repository, atLeastOnce()).findById(usuarioRequestMock.getId());
        verify(repository, atLeastOnce()).save(any(Usuario.class));
        verify(hateoasLinks, times(1)).links(any(UsuarioResponseDTO.class));

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
    @DisplayName("Retorna uma exceções UseNotFoundExcepion e EmailCannotBeDuplicateException")
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

            Exception ex2 = assertThrows(EmailCannotBeDuplicatedException.class, () ->{
                service.updateUserById(usuarioRequestMock.getId(), usuarioRequestMock);
            });

            assertEquals("O email fornecido já está cadastrado na base de dados", ex2.getMessage());
            verify(repository, atLeastOnce()).findByEmail(usuarioRequestMock.getEmail());
        }

        @Test
        @DisplayName("Lança uma exceção UserNotFound se nenhum usuário for encontrado na busca")
        void lanca_uma_excecao_se_nenhum_usuario_for_encontrado() {
            when(repository.findAll()).thenReturn(List.of());

            Exception ex = assertThrows(NotFoundException.class, () ->
                    service.findAllUsers()
            );

            assertEquals("Nenhum usuário encontrada no banco de dados", ex.getMessage());
            verify(repository, atLeastOnce()).findAll();
        }

        @Test
        @DisplayName("Lança uma exceção UserNotFound se o usuário especificado por ID não for encontrado")
        void lanca_uma_excecao_se_o_usuario_nao_for_encontrado() {
            when(repository.findById(usuarioRequestMock.getId())).thenReturn(Optional.empty());

            Exception ex1 = assertThrows(NotFoundException.class, () ->
                service.findUserById(usuarioRequestMock.getId())
            );

            assertEquals("Usuário não encontrado", ex1.getMessage());

            Exception ex2 = assertThrows(NotFoundException.class, () ->
                    service.updateUserById(usuarioRequestMock.getId(), usuarioRequestMock)
            );

            assertEquals("Usuário não encontrado", ex2.getMessage());

            Exception ex3 = assertThrows(NotFoundException.class, () ->
                    service.deleteUserById(usuarioRequestMock.getId())
            );

            assertEquals("Usuário não encontrado", ex3.getMessage());

            verify(repository, times(3)).findById(usuarioRequestMock.getId());
        }
    }
}