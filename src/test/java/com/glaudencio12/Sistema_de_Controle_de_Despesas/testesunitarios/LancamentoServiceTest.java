package com.glaudencio12.Sistema_de_Controle_de_Despesas.testesunitarios;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.request.LancamentoRequestDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response.LancamentoResponseDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.exception.NotFoundElementException;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.Categoria;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.Lancamento;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.Usuario;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.repository.CategoriaRepository;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.repository.LancamentoRepository;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.repository.UsuarioRepository;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.services.LancamentoService;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.testesunitarios.stubs.StubsCategoria;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.testesunitarios.stubs.StubsLancamento;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.testesunitarios.stubs.StubsUsuario;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.utils.HateoasLinks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@DisplayName("Testes da LancamentoService")
class LancamentoServiceTest {

    @Mock
    LancamentoRepository lancamentoRepository;

    @Mock
    HateoasLinks hateoasLinks;

    @Mock
    PagedResourcesAssembler<LancamentoResponseDTO> assembler;

    @Mock
    ModelMapper modelMapper;

    @Mock
    UsuarioRepository usuarioRepository;

    @Mock
    CategoriaRepository categoriaRepository;

    @Mock
    Authentication authentication;

    @Mock
    SecurityContext securityContext;

    @InjectMocks
    LancamentoService service;

    Lancamento lancamento;
    LancamentoRequestDTO lancamentoRequest;
    LancamentoResponseDTO lancamentoResponse;
    Usuario usuario;
    Categoria categoria;
    List<Lancamento> lancamentoList;

    @BeforeEach
    void setUp() {
        lancamento = StubsLancamento.lancamentoEntidade();
        lancamentoRequest = StubsLancamento.lancamentoRequestDTO();
        lancamentoResponse = StubsLancamento.lancamentoResponseDTO();
        usuario = StubsUsuario.usuarioEntidade();
        categoria = StubsCategoria.categoriaEntidade();
        lancamentoList = StubsLancamento.lancamentoEntidadeList();
    }

    public static void verificarLinks(LancamentoResponseDTO dto, String tipo, String rel, String href) {
        assertTrue(dto.getLinks().stream().anyMatch(link ->
                link.getType().equals(tipo) &&
                link.getRel().value().equals(rel) &&
                link.getHref().endsWith(href)
        ));
    }

    @Test
    @DisplayName("Deve registrar um novo lançamento com sucesso")
    void registrar_um_novo_lancamento() {
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(usuario));
        when(categoriaRepository.findByNome(anyString())).thenReturn(categoria);
        when(modelMapper.map(any(LancamentoRequestDTO.class), eq(Lancamento.class))).thenReturn(lancamento);
        when(lancamentoRepository.save(any(Lancamento.class))).thenReturn(lancamento);
        doCallRealMethod().when(hateoasLinks).links(any(LancamentoResponseDTO.class));

        LancamentoResponseDTO resposta = service.createLaunch(lancamentoRequest);

        assertNotNull(resposta);
        assertNotNull(resposta.getLinks());
        verify(usuarioRepository, atLeastOnce()).findById(anyLong());
        verify(categoriaRepository, atLeastOnce()).findByNome(anyString());
        verify(modelMapper, atLeastOnce()).map(any(LancamentoRequestDTO.class), eq(Lancamento.class));
        verify(lancamentoRepository, atLeastOnce()).save(any(Lancamento.class));
        verify(hateoasLinks, atLeastOnce()).links(any(LancamentoResponseDTO.class));

        verificarLinks(resposta, "GET", "FindLaunchById", "/api/lancamentos/1");
        verificarLinks(resposta, "GET", "FindAllLaunchs", "/api/lancamentos");
        verificarLinks(resposta, "POST", "CreateLaunch", "/api/lancamentos");
    }

    @Test
    @DisplayName("Deve buscar um lançamento por ID com sucesso")
    void busca_uma_lancamento_por_id() {
        when(lancamentoRepository.findById(anyLong())).thenReturn(Optional.of(lancamento));
        doCallRealMethod().when(hateoasLinks).links(any(LancamentoResponseDTO.class));

        LancamentoResponseDTO resposta = service.findLaunchById(1L);

        assertNotNull(resposta);
        assertNotNull(resposta.getLinks());
        verify(lancamentoRepository, atLeastOnce()).findById(anyLong());
        verify(hateoasLinks, atLeastOnce()).links(any(LancamentoResponseDTO.class));

        verificarLinks(resposta, "GET", "FindLaunchById", "/api/lancamentos/1");
        verificarLinks(resposta, "GET", "FindAllLaunchs", "/api/lancamentos");
        verificarLinks(resposta, "POST", "CreateLaunch", "/api/lancamentos");
    }

    @Test
    @DisplayName("Deve buscar todos os lançamentos com paginação")
    void busca_todos_os_lacamentos_registrados_usando_paginacao() {
        Pageable pageable = PageRequest.of(0, 10);

        when(authentication.getName()).thenReturn("email@teste.com");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("email@teste.com");

        when(usuarioRepository.findByEmail("email@teste.com")).thenReturn(usuario);

        Page<Lancamento> page = new PageImpl<>(lancamentoList);

        when(lancamentoRepository.findByUsuarioId(usuario.getId(), pageable)).thenReturn(page);
        when(modelMapper.map(any(Lancamento.class), eq(LancamentoResponseDTO.class))).thenReturn(lancamentoResponse);
        doNothing().when(hateoasLinks).links(any(LancamentoResponseDTO.class));

        List<LancamentoResponseDTO> lancamentosDTO = page.stream().map(lancamento ->
                modelMapper.map(lancamento, LancamentoResponseDTO.class)
        ).toList();

        PagedModel<EntityModel<LancamentoResponseDTO>> pagedModel = PagedModel.of(
                lancamentosDTO.stream().map(EntityModel::of).toList(),
                new PagedModel.PageMetadata(10, 0, lancamentosDTO.size())
        );

        when(assembler.toModel(any(Page.class))).thenReturn(pagedModel);

        PagedModel<EntityModel<LancamentoResponseDTO>> resposta = service.findAllLaunchesPage(pageable);

        assertNotNull(resposta);
        assertEquals(10, resposta.getContent().size());
        verify(hateoasLinks, times(10)).links(any(LancamentoResponseDTO.class));
        verify(modelMapper, times(10)).map(any(Lancamento.class), eq(LancamentoResponseDTO.class));
        verify(assembler).toModel(any(Page.class));
        verify(lancamentoRepository).findByUsuarioId(usuario.getId(), pageable);
    }

    @Nested
    @DisplayName("Exceções de NotFound em Lançamento")
    class LancamentoDeExcecaoNotFound {

        @DisplayName("Deve lançar exceção se o lançamento não for encontrado por ID")
        @Test
        void deve_lancar_excecao_quando_buscar_por_id_e_nao_existir() {

            when(lancamentoRepository.findById(1L)).thenReturn(Optional.empty());

            Exception ex = assertThrows(NotFoundElementException.class,
                    () -> service.findLaunchById(1L)
            );

            assertEquals("Lançamento não encontrado", ex.getMessage());
            verify(lancamentoRepository).findById(1L);
        }


        @DisplayName("Deve lançar exceção se não houver lançamentos cadastrados para o usuário")
        @Test
        void deve_lancar_excecao_quando_buscar_todos_e_nao_houver_lancamentos() {

            Pageable pageable = PageRequest.of(0, 10);

            when(authentication.getName()).thenReturn("email@teste.com");
            when(securityContext.getAuthentication()).thenReturn(authentication);
            SecurityContextHolder.setContext(securityContext);

            Usuario usuario = new Usuario();
            usuario.setId(1L);
            usuario.setEmail("email@teste.com");

            when(usuarioRepository.findByEmail("email@teste.com")).thenReturn(usuario);
            when(lancamentoRepository.findByUsuarioId(usuario.getId(), pageable)).thenReturn(Page.empty());

            Exception ex = assertThrows(NotFoundElementException.class,
                    () -> service.findAllLaunchesPage(pageable)
            );

            assertEquals("Nenhum lançamento encontrado para este usuário", ex.getMessage());
            verify(lancamentoRepository).findByUsuarioId(usuario.getId(), pageable);
        }

        @DisplayName("Deve lançar exceção ao criar lançamento se o usuário não existir")
        @Test
        void deve_lancar_excecao_ao_criar_lancamento_quando_usuario_nao_existe() {

            when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

            Exception ex = assertThrows(NotFoundElementException.class, () ->
                    service.createLaunch(lancamentoRequest)
            );

            assertEquals("Usuário não encontrado", ex.getMessage());
            verify(usuarioRepository).findById(1L);
        }


        @Test
        @DisplayName("Deve lançar exceção se a categoria não for encontrada")
        void lanca_excecao_se_a_categoria_nao_for_encontrada() {

            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
            when(categoriaRepository.findByNome(categoria.getNome())).thenReturn(null);

            Exception ex = assertThrows(NotFoundElementException.class, () ->
                    service.createLaunch(lancamentoRequest)
            );

            assertEquals("Categoria não encontrada", ex.getMessage());
            verify(categoriaRepository).findByNome(categoria.getNome());
        }

    }
}
