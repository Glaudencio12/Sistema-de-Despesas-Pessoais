package com.glaudencio12.Sistema_de_Controle_de_Despesas.services;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.request.CategoriaRequestDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response.CategoriaResponseDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.exception.CategoryCannotBeDuplicateException;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.exception.NotFoundElementException;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.Categoria;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.Usuario;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.repository.CategoriaRepository;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.repository.UsuarioRepository;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.utils.HateoasLinks;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class CategoriaService {
    private static final Logger logger = LoggerFactory.getLogger(CategoriaService.class.getName());

    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;
    private final HateoasLinks hateoasLinks;
    private final ModelMapper modelMapper;
    private final PagedResourcesAssembler<CategoriaResponseDTO> assembler;

    public CategoriaService(
            CategoriaRepository repository,
            UsuarioRepository usuarioRepository,
            HateoasLinks hateoasLinks,
            ModelMapper modelMapper,
            PagedResourcesAssembler<CategoriaResponseDTO> assembler
    ) {
        this.categoriaRepository = repository;
        this.usuarioRepository = usuarioRepository;
        this.hateoasLinks = hateoasLinks;
        this.modelMapper = modelMapper;
        this.assembler = assembler;
    }

    public CategoriaResponseDTO createCategory(CategoriaRequestDTO categoriaRequest) {
        logger.info("Iniciando cadastro de categoria com o nome de {}", categoriaRequest.getNome());
        Categoria categoriaCadastrada = categoriaRepository.findByNome(categoriaRequest.getNome());
        usuarioRepository.findById(categoriaRequest.getUsuarioId()).orElseThrow(() -> new NotFoundElementException("O usuário informado não foi encontrado na base de dados"));

        if (categoriaCadastrada != null) {
            throw new CategoryCannotBeDuplicateException("A categoria fornecida já está cadastrada na base de dados");
        } else {
            Categoria entidade = categoriaRepository.save(modelMapper.map(categoriaRequest, Categoria.class));
            CategoriaResponseDTO dto = modelMapper.map(entidade, CategoriaResponseDTO.class);
            hateoasLinks.links(dto);
            return dto;
        }
    }

    public CategoriaResponseDTO findCategoryById(Long id) {
        logger.info("Iniciando a busca da categoria com id {}", id);
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new NotFoundElementException("Categoria não encontrada"));
        CategoriaResponseDTO dto = modelMapper.map(categoria, CategoriaResponseDTO.class);
        hateoasLinks.links(dto);
        return dto;
    }

    public PagedModel<EntityModel<CategoriaResponseDTO>> findAllCategories(Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        logger.info("Buscando todas as categorias do usuário autenticado");

        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            throw new NotFoundElementException("Usuário não encontrado");
        }

        Page<Categoria> categorias = categoriaRepository.findByUsuarioId(usuario.getId(), pageable);
        if (categorias.isEmpty()) {
            throw new NotFoundElementException("Nenhuma categoria encontrada para este usuário");
        } else {
            Page<CategoriaResponseDTO> categoriaResponse = categorias.map(categoria -> {
                var categoriaDTO = modelMapper.map(categoria, CategoriaResponseDTO.class);
                hateoasLinks.links(categoriaDTO);
                return categoriaDTO;
            });
            return assembler.toModel(categoriaResponse);
        }
    }

    public void deleteCategoryByName(String nomeCategoria) {
        logger.info("Excluindo a categoria de nome {}", nomeCategoria);
        Categoria categoria = categoriaRepository.findByNome(nomeCategoria);
        if (categoria == null) {
            throw new NotFoundElementException("A categoria fornecida não foi cadastrada");
        } else {
            categoriaRepository.delete(categoria);
        }
    }

}
