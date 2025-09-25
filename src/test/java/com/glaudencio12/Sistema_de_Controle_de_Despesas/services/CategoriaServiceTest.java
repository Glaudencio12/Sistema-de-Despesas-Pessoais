package com.glaudencio12.Sistema_de_Controle_de_Despesas.services;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.request.CategoriaRequestDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response.CategoriaResponseDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.exception.CategoryCannotBeDuplicateException;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.exception.NotFoundElementException;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.Categoria;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.Usuario;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.repository.CategoriaRepository;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.repository.UsuarioRepository;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.stubs.StubsCategoria;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.stubs.StubsUsuario;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.utils.HateoasLinks;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class CategoriaServiceTest {

    @Mock
    CategoriaRepository categoriaRepository;

    @Mock
    UsuarioRepository usuarioRepository;

    @Mock
    HateoasLinks hateoasLinks;

    @Mock
    ModelMapper modelMapper;

    @Mock
    PagedResourcesAssembler<CategoriaResponseDTO> assembler;

    @Mock
    UsuarioService usuarioService;

    @InjectMocks
    CategoriaService categoriaService;

    Usuario usuario;
    Categoria categoria;
    CategoriaRequestDTO categoriaRequest;
    CategoriaResponseDTO categoriaResponse;
    List<Categoria> categoriaList;

    @BeforeEach
    void setUp() {
        usuario = StubsUsuario.usuarioEntidade();
        categoria = StubsCategoria.categoriaEntidade();
        categoriaList = StubsCategoria.categoriaEntidadeList();
        categoriaRequest = StubsCategoria.categoriaRequest();
        categoriaResponse = StubsCategoria.categoriaResponse();
        categoriaList = StubsCategoria.categoriaEntidadeList();
    }

    public static void verificarLinks(CategoriaResponseDTO dto, String rel, String href, String type) {
        assertTrue(dto.getLinks().stream().anyMatch(link ->
                link.getRel().value().equals(rel) &
                link.getHref().endsWith(href) &
                link.getType().equals(type)
        ));
    }

    @Test
    @DisplayName("Deve criar uma nova categoria com sucesso")
    void cria_uma_categoria() {
        when(categoriaRepository.findByNome(categoriaRequest.getNome())).thenReturn(null);
        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario));
        when(modelMapper.map(any(CategoriaRequestDTO.class), eq(Categoria.class))).thenReturn(categoria);
        when(modelMapper.map(any(Categoria.class), eq(CategoriaResponseDTO.class))).thenReturn(categoriaResponse);
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);
        doCallRealMethod().when(hateoasLinks).links(any(CategoriaResponseDTO.class));

        CategoriaResponseDTO resposta = categoriaService.createCategory(categoriaRequest);

        assertNotNull(resposta);
        assertNotNull(resposta.getLinks());
        verify(usuarioRepository, atLeastOnce()).findById(anyLong());
        verify(categoriaRepository, atLeastOnce()).save(any(Categoria.class));
        verify(hateoasLinks, atLeastOnce()).links(any(CategoriaResponseDTO.class));
        verify(modelMapper, times(1)).map(any(Categoria.class), eq(CategoriaResponseDTO.class));
        verify(modelMapper, times(1)).map(any(CategoriaRequestDTO.class), eq(Categoria.class));


        verificarLinks(resposta, "FindCategoryById", "/api/categorias/1", "GET");
        verificarLinks(resposta, "FindAllCategorys", "/api/categorias", "GET");
        verificarLinks(resposta, "CreateCategory", "/api/categorias", "POST");
        verificarLinks(resposta, "DeleteCategory", "/api/categorias/Categoria%20teste", "DELETE");
    }

    @Test
    @DisplayName("Deve buscar uma categoria existente pelo id")
    void busca_uma_categoria() {
        when(categoriaRepository.findById(anyLong())).thenReturn(Optional.of(categoria));
        when(modelMapper.map(any(Categoria.class), eq(CategoriaResponseDTO.class))).thenReturn(categoriaResponse);

        doCallRealMethod().when(hateoasLinks).links(any(CategoriaResponseDTO.class));

        CategoriaResponseDTO resposta = categoriaService.findCategoryById(anyLong());

        assertNotNull(resposta);
        assertNotNull(resposta.getLinks());
        verify(categoriaRepository, atLeastOnce()).findById(anyLong());
        verify(hateoasLinks, atLeastOnce()).links(any(CategoriaResponseDTO.class));
        verify(modelMapper, times(1)).map(any(Categoria.class), eq(CategoriaResponseDTO.class));

        verificarLinks(resposta, "FindCategoryById", "/api/categorias/1", "GET");
        verificarLinks(resposta, "FindAllCategorys", "/api/categorias", "GET");
        verificarLinks(resposta, "CreateCategory", "/api/categorias", "POST");
        verificarLinks(resposta, "DeleteCategory", "/api/categorias/Categoria%20teste", "DELETE");
    }

    @Test
    @DisplayName("Deve buscar todas as categorias cadastradas")
    void busca_todas_as_categorias_cadastradas_usando_paginacao() {
        Pageable pageable = PageRequest.of(0,10);
        Page<Categoria> page = new PageImpl<>(categoriaList);

        when(categoriaRepository.findAll(pageable)).thenReturn(page);
        when(modelMapper.map(any(Categoria.class), eq(CategoriaResponseDTO.class))).thenReturn(categoriaResponse);
        doNothing().when(hateoasLinks).links(any(CategoriaResponseDTO.class));

        List<CategoriaResponseDTO> categoriaDTO = page.stream().map(categoria -> modelMapper.map(categoria, CategoriaResponseDTO.class)).toList();

        PagedModel<EntityModel<CategoriaResponseDTO>> pagedModel = PagedModel.of(
                categoriaDTO.stream().map(EntityModel::of).toList(),
                new PagedModel.PageMetadata(10, 0, categoriaDTO.size())
        );

        when(assembler.toModel(any(Page.class))).thenReturn(pagedModel);

        PagedModel<EntityModel<CategoriaResponseDTO>> resposta = categoriaService.findAllCategories(pageable);

        assertNotNull(resposta);
        assertEquals(10, resposta.getContent().size());
        verify(categoriaRepository).findAll(pageable);
        verify(hateoasLinks, times(10)).links(any(CategoriaResponseDTO.class));
        verify(modelMapper, times(20)).map(any(Categoria.class), eq(CategoriaResponseDTO.class));
        verify(assembler).toModel(any(Page.class));
    }

    @Test
    @DisplayName("Deve excluir uma categoria pelo nome")
    void exclui_uma_categoria() {
        when(categoriaRepository.findByNome(categoriaRequest.getNome())).thenReturn(categoria);
        doNothing().when(categoriaRepository).delete(any(Categoria.class));

        categoriaService.deleteCategoryByName(categoria.getNome());

        verify(categoriaRepository, atLeastOnce()).findByNome(anyString());
        verify(categoriaRepository, atLeastOnce()).delete(any(Categoria.class));
    }

    @Test
    @DisplayName("Deve lançar CategoryCannotBeDuplicateException quando tentar criar categoria duplicada")
    void lanca_exececao_de_tiver_categoria_duplicada() {
        when(usuarioRepository.findById(categoriaRequest.getUsuarioId())).thenReturn(Optional.of(usuario));
        when(categoriaRepository.findByNome(categoriaRequest.getNome())).thenReturn(categoria);

        Exception ex = assertThrows(CategoryCannotBeDuplicateException.class,
                () -> categoriaService.createCategory(categoriaRequest)
        );
        assertEquals("A categoria fornecida já está cadastrada na base de dados", ex.getMessage());
        verify(categoriaRepository, atLeastOnce()).findByNome(anyString());
    }

    @Nested
    @DisplayName("Testes de exceção quando elementos não são encontrados")
    class NotFound {

        @Test
        @DisplayName("Deve lançar NotFoundElementException se o usuário não for encontrado ou categoria não existir")
        void lanca_excecao_se_o_usuario_nao_for_encontrado() {

            when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.empty());
            when(categoriaRepository.findByNome(categoriaRequest.getNome())).thenReturn(null);

            Exception ex1 = assertThrows(NotFoundElementException.class,
                    () -> categoriaService.createCategory(categoriaRequest)
            );
            assertEquals("O usuário informado não foi encontrado na base de dados", ex1.getMessage());

            when(categoriaRepository.findById(anyLong())).thenReturn(Optional.empty());
            Exception ex2 = assertThrows(NotFoundElementException.class,
                    () -> categoriaService.findCategoryById(anyLong())
            );
            assertEquals("Categoria não encontrada", ex2.getMessage());

            when(categoriaRepository.findByNome(categoriaRequest.getNome())).thenReturn(null);
            Exception ex3 = assertThrows(NotFoundElementException.class,
                    () -> categoriaService.deleteCategoryByName(categoriaRequest.getNome())
            );
            assertEquals("A categoria fornecida não foi cadastrada", ex3.getMessage());

            Pageable pageable = PageRequest.of(0, 10);
            Page<Categoria> page = Page.empty();
            when(categoriaRepository.findAll(pageable)).thenReturn(page);
            Exception ex4 = assertThrows(NotFoundElementException.class,
                   () -> categoriaService.findAllCategories(pageable)
            );
            assertEquals("Nenhuma categoria encontrada", ex4.getMessage());
        }
    }

}