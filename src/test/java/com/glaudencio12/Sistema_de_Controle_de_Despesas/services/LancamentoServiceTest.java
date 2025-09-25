package com.glaudencio12.Sistema_de_Controle_de_Despesas.services;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.request.LancamentoRequestDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response.LancamentoResponseDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.exception.NotFoundElementException;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.Categoria;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.Lancamento;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.Usuario;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.repository.CategoriaRepository;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.repository.LancamentoRepository;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.repository.UsuarioRepository;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.stubs.StubsCategoria;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.stubs.StubsLancamento;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.stubs.StubsUsuario;
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

    public static void verificarLinks(LancamentoResponseDTO dto, String tipo, String rel, String href){
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

        verificarLinks(resposta,"GET", "FindLaunchById", "/api/lancamentos/1");
        verificarLinks(resposta,"GET", "FindAllLaunchs", "/api/lancamentos");
        verificarLinks(resposta,"POST", "CreateLaunch", "/api/lancamentos");
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

        verificarLinks(resposta,"GET", "FindLaunchById", "/api/lancamentos/1");
        verificarLinks(resposta,"GET", "FindAllLaunchs", "/api/lancamentos");
        verificarLinks(resposta,"POST", "CreateLaunch", "/api/lancamentos");
    }

    @Test
    @DisplayName("Deve buscar todos os lançamentos com paginação")
    void busca_todos_os_lacamentos_registrados_usando_paginacao() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Lancamento> page = new PageImpl<>(lancamentoList);

        when(lancamentoRepository.findAll(pageable)).thenReturn(page);
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

        PagedModel<EntityModel<LancamentoResponseDTO>> resposta = service.findAllLaunches(pageable);

        assertNotNull(resposta);
        assertEquals(10, resposta.getContent().size());
        verify(lancamentoRepository).findAll(pageable);
        verify(hateoasLinks, times(10)).links(any(LancamentoResponseDTO.class));
        verify(modelMapper, times(10)).map(any(Lancamento.class), eq(LancamentoResponseDTO.class));
        verify(assembler).toModel(any(Page.class));
    }

    @Nested
    @DisplayName("Exceções de NotFound em Lançamento")
    class LancamentoDeExcecaoNotFound{

        @Test
        @DisplayName("Deve lançar exceção se o lançamento não for encontrado por ID, se não houver lançamentos cadastrados ou se o usuário não existir")
        void lanca_excecao_se_o_lancamento_nao_for_encontrado(){
            Pageable pageble = PageRequest.of(0, 10);
            Page<Lancamento> page = Page.empty();

            when(lancamentoRepository.findById(anyLong())).thenReturn(Optional.empty());
            when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());
            when(lancamentoRepository.findAll(pageble)).thenReturn(page);

            Exception ex1 = assertThrows(NotFoundElementException.class, () ->
                    service.findLaunchById(1L)
            );
            assertEquals("Lançamento não encontrado", ex1.getMessage());
            verify(lancamentoRepository, atLeastOnce()).findById(anyLong());

            Exception ex2 = assertThrows(NotFoundElementException.class, () ->
                    service.findAllLaunches(pageble)
            );
            assertEquals("Nenhum lançameto encontrado", ex2.getMessage());
            verify(lancamentoRepository, atLeastOnce()).findAll(pageble);

            Exception ex3 = assertThrows(NotFoundElementException.class, () ->
                    service.createLaunch(lancamentoRequest)
            );
            assertEquals("Usuário não encontrado", ex3.getMessage());
            verify(usuarioRepository, atLeastOnce()).findById(anyLong());
        }

        @Test
        @DisplayName("Deve lançar exceção se a categoria não for encontrada")
        void lanca_excecao_se_a_categoria_nao_for_encontrada(){
            when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(usuario));
            when(categoriaRepository.findByNome(anyString())).thenReturn(null);

            Exception ex = assertThrows(NotFoundElementException.class, () ->
                    service.createLaunch(lancamentoRequest)
            );
            assertEquals("Categoria não encontrada", ex.getMessage());
            verify(categoriaRepository, atLeastOnce()).findByNome(anyString());
        }
    }
}
