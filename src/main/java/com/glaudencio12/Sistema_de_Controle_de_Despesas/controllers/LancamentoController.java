package com.glaudencio12.Sistema_de_Controle_de_Despesas.controllers;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.controllers.docs.LancamentoControllerDocs;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.request.LancamentoRequestDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response.LancamentoResponseDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.services.LancamentoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@RestController
@RequestMapping("/api/lancamentos")
@Tag(name = "Lançamentos", description = "Esta seção reúne os endpoints responsáveis pelas operações de adição e consulta de lançamentos.")
public class LancamentoController implements LancamentoControllerDocs {
    private final LancamentoService service;

    public LancamentoController(LancamentoService service) {
        this.service = service;
    }

    @PostMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<LancamentoResponseDTO> create(@RequestBody @Valid LancamentoRequestDTO lancamento){
        LancamentoResponseDTO lancamentoResponse = service.createLaunch(lancamento);
        return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoResponse);
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<LancamentoResponseDTO> findById(@PathVariable("id") Long id){
        LancamentoResponseDTO lancamentoResponse = service.findLaunchById(id);
        return ResponseEntity.ok(lancamentoResponse);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PagedModel<EntityModel<LancamentoResponseDTO>>> findAll(Pageable pageable){
        PagedModel<EntityModel<LancamentoResponseDTO>> lancamentosResponse = service.findAllLaunchesPage(pageable);
        return ResponseEntity.ok(lancamentosResponse);
    }

    @GetMapping(value = "/despesaTotal", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public BigDecimal balanceExpenses() {
        return null;
    }

    @GetMapping(value = "/receitaTotal", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public BigDecimal revenueBalance() {
        return null;
    }
}
