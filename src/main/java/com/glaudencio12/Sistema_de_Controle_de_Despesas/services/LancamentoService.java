package com.glaudencio12.Sistema_de_Controle_de_Despesas.services;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.request.LancamentoRequestDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response.LancamentoResponseDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.exception.NotFoundElementException;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.mapper.LancamentoMapper;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.mapper.ObjectMapper;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.Categoria;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.Lancamento;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.Usuario;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.repository.CategoriaRepository;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.repository.LancamentoRepository;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.repository.UsuarioRepository;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.utils.DataFormatada;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.utils.HateoasLinks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class LancamentoService {
    private static final Logger logger = LoggerFactory.getLogger(LancamentoService.class.getName());

    private final LancamentoRepository lancamentoRepository;
    private final HateoasLinks hateoasLinks;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;

    public LancamentoService(LancamentoRepository repository, HateoasLinks hateoasLinks, UsuarioRepository usuarioRepository, CategoriaRepository categoriaRepository) {
        this.lancamentoRepository = repository;
        this.hateoasLinks = hateoasLinks;
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public LancamentoResponseDTO createLaunch(LancamentoRequestDTO lancamento) {
        logger.info("Registrando um lançamento do tipo {}", lancamento.getTipo());
        Usuario usuario = usuarioRepository.findById(lancamento.getUsuarioId()).orElseThrow(() -> new NotFoundElementException("Usuário não encontrado"));
        Categoria categoria = categoriaRepository.findByNome(lancamento.getCategoria());

        if (categoria == null || !lancamento.getTipo().equals(categoria.getTipo())) {
            throw new NotFoundElementException("Categoria não encontrada");
        }

        Lancamento entidade = ObjectMapper.parseObject(lancamento, Lancamento.class);
        entidade.setData(LocalDateTime.now());
        entidade.setUsuario(usuario);
        entidade.setCategoria(categoria);

        Lancamento salvo = lancamentoRepository.save(entidade);
        LancamentoResponseDTO dto = LancamentoMapper.toResponseDTO(salvo);
        dto.setData(DataFormatada.data(salvo.getData()));
        hateoasLinks.links(dto);
        return dto;
    }

    public LancamentoResponseDTO findLaunchById(Long id) {
        logger.info("Buscando um lançamento com id {}", id);
        Lancamento lancamento = lancamentoRepository.findById(id).orElseThrow(() -> new NotFoundElementException("Lançamento não encontrado"));
        Lancamento salvo = lancamentoRepository.save(lancamento);
        LancamentoResponseDTO dto = LancamentoMapper.toResponseDTO(salvo);
        hateoasLinks.links(dto);
        return dto;
    }

    public List<LancamentoResponseDTO> findAllLaunches() {
        logger.info("Buscando todos os lançamentos registrados");
        List<Lancamento> lancamentos = lancamentoRepository.findAll();
        if (lancamentos.isEmpty()) {
            throw new NotFoundElementException("Nenhum lançameto encontrado");
        }

        List<LancamentoResponseDTO> lancamentosDTO = new ArrayList<>();
        lancamentos.forEach(lancamento -> lancamentosDTO.add(LancamentoMapper.toResponseDTO(lancamento)));
        lancamentosDTO.forEach(hateoasLinks::links);
        return lancamentosDTO;
    }
}
