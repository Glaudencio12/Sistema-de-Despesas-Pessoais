package com.glaudencio12.Sistema_de_Controle_de_Despesas.services;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.request.CategoriaRequestDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response.CategoriaResponseDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.exception.NotFoundException;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.mapper.ObjectMapper;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.Categoria;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.repository.CategoriaRepository;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.utils.HateoasLinks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {
    Logger logger = LoggerFactory.getLogger(CategoriaService.class.getName());

    private final CategoriaRepository repository;
    private final HateoasLinks hateoasLinks;

    public CategoriaService(CategoriaRepository repository, HateoasLinks hateoasLinks) {
        this.repository = repository;
        this.hateoasLinks = hateoasLinks;
    }

    public CategoriaResponseDTO createCategory(CategoriaRequestDTO categoriaRequest) {
        logger.info("Iniciando cadastro de categoria com o nome de {}", categoriaRequest.getNome());
        Categoria categoriaCadastrada = repository.findByNome(categoriaRequest.getNome());
        if (categoriaCadastrada != null) {
            throw new RuntimeException("A categoria fornecida já está cadastrada na base de dados");
        } else {
            Categoria entidade = repository.save(ObjectMapper.parseObject(categoriaRequest, Categoria.class));
            CategoriaResponseDTO dto = ObjectMapper.parseObject(entidade, CategoriaResponseDTO.class);
            hateoasLinks.links(dto);
            return dto;
        }
    }

    public CategoriaResponseDTO findCatgeoryById(Long id) {
        logger.info("Iniciando a busca da categoria com id {}", id);
        Categoria categoria = repository.findById(id).orElseThrow(() -> new NotFoundException("Categoria não encontrada"));
        CategoriaResponseDTO dto = ObjectMapper.parseObject(categoria, CategoriaResponseDTO.class);
        hateoasLinks.links(dto);
        return dto;
    }

    public List<CategoriaResponseDTO> findAllCatgorys() {
        logger.info("Buscando todas as categorias no banco");
        List<Categoria> categorias = repository.findAll();
        if (categorias.isEmpty()) {
            throw new NotFoundException("Nenhuma categoria encontrada");
        } else {
            List<CategoriaResponseDTO> dtos = ObjectMapper.parseObjects(categorias, CategoriaResponseDTO.class);
            dtos.forEach(hateoasLinks::links);
            return dtos;
        }
    }

    public void deleteCategoryByName(String nomeCategoria) {
        Categoria categoria = repository.findByNome(nomeCategoria);
        if (categoria == null) {
            throw new NotFoundException("A categoria fornecida não foi cadastrada");
        } else {
            repository.delete(categoria);
        }
    }
}
