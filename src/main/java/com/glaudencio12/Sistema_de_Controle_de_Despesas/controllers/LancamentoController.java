package com.glaudencio12.Sistema_de_Controle_de_Despesas.controllers;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.controllers.docs.LancamentoControllerDocs;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.request.LancamentoRequestDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response.LancamentoResponseDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.services.LancamentoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lancamentos")
@Tag(name = "Lançamentos", description = "Esta seção reúne os endpoints responsáveis pelas operações de adição e consulta de lançamentos.")
public class LancamentoController implements LancamentoControllerDocs {
    private final LancamentoService service;

    public LancamentoController(LancamentoService service) {
        this.service = service;
    }

    @PostMapping
    @Override
    public LancamentoResponseDTO create(@RequestBody @Valid LancamentoRequestDTO lancamento){
        return service.createLaunch(lancamento);
    }

    @GetMapping("/{id}")
    @Override
    public LancamentoResponseDTO findById(@PathVariable("id") Long id){
        return service.findLaunchById(id);
    }

    @GetMapping
    @Override
    public List<LancamentoResponseDTO> findAll(){
        return service.findAllLaunches();
    }
}
