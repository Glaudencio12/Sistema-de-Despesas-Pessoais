package com.glaudencio12.Sistema_de_Controle_de_Despesas.services;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.request.LancamentoRequestDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response.LancamentoResponseDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.exception.NotFoundElementException;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.mapper.LancamentoMapper;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.Categoria;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.Lancamento;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.Usuario;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.enums.TipoLancamentoCategoria;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.repository.CategoriaRepository;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.repository.LancamentoRepository;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.repository.UsuarioRepository;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.utils.DataFormatada;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LancamentoService {
    private static final Logger logger = LoggerFactory.getLogger(LancamentoService.class.getName());

    private final LancamentoRepository lancamentoRepository;
    private final HateoasLinks hateoasLinks;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final ModelMapper modelMapper;
    private final PagedResourcesAssembler<LancamentoResponseDTO> assembler;

    public LancamentoService(
            LancamentoRepository repository,
            HateoasLinks hateoasLinks,
            UsuarioRepository usuarioRepository,
            CategoriaRepository categoriaRepository,
            ModelMapper modelMapper,
            PagedResourcesAssembler<LancamentoResponseDTO> assembler
    ) {
        this.lancamentoRepository = repository;
        this.hateoasLinks = hateoasLinks;
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
        this.modelMapper = modelMapper;
        this.assembler = assembler;
    }

    public LancamentoResponseDTO createLaunch(LancamentoRequestDTO lancamento) {
        logger.info("Registrando um lançamento do tipo {}", lancamento.getTipo());
        Usuario usuario = usuarioRepository.findById(lancamento.getUsuarioId()).orElseThrow(() -> new NotFoundElementException("Usuário não encontrado"));
        Categoria categoria = categoriaRepository.findByNome(lancamento.getCategoria());

        if (categoria == null || !lancamento.getTipo().equals(categoria.getTipo())) {
            throw new NotFoundElementException("Categoria não encontrada");
        }

        Lancamento entidade = modelMapper.map(lancamento, Lancamento.class);
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
        LancamentoResponseDTO dto = LancamentoMapper.toResponseDTO(lancamento);
        hateoasLinks.links(dto);
        return dto;
    }

    private Usuario authenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        logger.info("Buscando dados do usuário autenticado no sistema: {}", email);

        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            throw  new NotFoundElementException("Usuário não encontrado");
        }

        logger.info("Usuário encontrado!");

        return usuario;
    }

    public PagedModel<EntityModel<LancamentoResponseDTO>> findAllLaunchesPage(Pageable pageable) {
        Page<Lancamento> lancamentos = lancamentoRepository.findByUsuarioId(authenticatedUser().getId(), pageable);

        if (lancamentos.isEmpty()) {
            throw new NotFoundElementException("Nenhum lançamento encontrado para este usuário");
        }else {
            Page<LancamentoResponseDTO> lancamentoResponse = lancamentos.map(lancamento -> {
                var lancamentoDTO = LancamentoMapper.toResponseDTO(lancamento);
                hateoasLinks.links(lancamentoDTO);
                return lancamentoDTO;
            });
            return assembler.toModel(lancamentoResponse);
        }
    }

    public BigDecimal balanceExpenses() {
        logger.info("Buscando lançamentos do usuário {} para cálculo de despesas", authenticatedUser().getEmail());

        BigDecimal valorTotal = lancamentoRepository.sumValorByUsuarioIdAndTipo(authenticatedUser().getId(), TipoLancamentoCategoria.DESPESA);

        logger.info("Saldo de despesas calculado com sucesso");

        return valorTotal != null ? valorTotal : BigDecimal.ZERO;
    }

    public BigDecimal revenueBalance() {
        logger.info("Buscando lançamentos do usuário {} para cálculo de receitas", authenticatedUser().getEmail());

        BigDecimal valorTotal = lancamentoRepository.sumValorByUsuarioIdAndTipo(authenticatedUser().getId(), TipoLancamentoCategoria.RECEITA);

        logger.info("Saldo de receitas calculado com sucesso");

        return valorTotal != null ? valorTotal : BigDecimal.ZERO;
    }
}
