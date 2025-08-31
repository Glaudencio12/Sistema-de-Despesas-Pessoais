package com.glaudencio12.Sistema_de_Controle_de_Despesas.services;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.request.CategoriaRequestDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response.CategoriaResponseDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.exception.CategoryCannotBeDuplicateException;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.exception.NotFoundElementException;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.mapper.ObjectMapper;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.Categoria;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.repository.CategoriaRepository;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.repository.UsuarioRepository;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.utils.HateoasLinks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {
    private static final Logger logger = LoggerFactory.getLogger(CategoriaService.class.getName());

    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;
    private final HateoasLinks hateoasLinks;

    public CategoriaService(CategoriaRepository repository, UsuarioRepository usuarioRepository, HateoasLinks hateoasLinks) {
        this.categoriaRepository = repository;
        this.usuarioRepository = usuarioRepository;
        this.hateoasLinks = hateoasLinks;
    }

    public CategoriaResponseDTO createCategory(CategoriaRequestDTO categoriaRequest) {
        logger.info("Iniciando cadastro de categoria com o nome de {}", categoriaRequest.getNome());
        Categoria categoriaCadastrada = categoriaRepository.findByNome(categoriaRequest.getNome());
        usuarioRepository.findById(categoriaRequest.getUsuarioId()).orElseThrow(() -> new NotFoundElementException("O usuário informado não foi encontrado na base de dados"));

        if (categoriaCadastrada != null) {
            throw new CategoryCannotBeDuplicateException("A categoria fornecida já está cadastrada na base de dados");
        } else {
            Categoria entidade = categoriaRepository.save(ObjectMapper.parseObject(categoriaRequest, Categoria.class));
            CategoriaResponseDTO dto = ObjectMapper.parseObject(entidade, CategoriaResponseDTO.class);
            hateoasLinks.links(dto);
            return dto;
        }
    }

    public CategoriaResponseDTO findCategoryById(Long id) {
        logger.info("Iniciando a busca da categoria com id {}", id);
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new NotFoundElementException("Categoria não encontrada"));
        CategoriaResponseDTO dto = ObjectMapper.parseObject(categoria, CategoriaResponseDTO.class);
        hateoasLinks.links(dto);
        return dto;
    }

    public List<CategoriaResponseDTO> findAllCategories() {
        logger.info("Buscando todas as categorias no banco");
        List<Categoria> categorias = categoriaRepository.findAll();
        if (categorias.isEmpty()) {
            throw new NotFoundElementException("Nenhuma categoria encontrada");
        } else {
            List<CategoriaResponseDTO> dtos = ObjectMapper.parseObjects(categorias, CategoriaResponseDTO.class);
            dtos.forEach(hateoasLinks::links);
            return dtos;
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
