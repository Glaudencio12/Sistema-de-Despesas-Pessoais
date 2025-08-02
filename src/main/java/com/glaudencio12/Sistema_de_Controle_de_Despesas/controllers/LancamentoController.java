package com.glaudencio12.Sistema_de_Controle_de_Despesas.controllers;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.request.LancamentoRequestDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response.LancamentoResponseDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.services.LancamentoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lancamentos")
@Tag(name = "Lançamentos", description = "Esta seção reúne os endpoints responsáveis pelas operações de adição e consulta de lançamentos.")
public class LancamentoController {
    private final LancamentoService service;

    public LancamentoController(LancamentoService service) {
        this.service = service;
    }

    @PostMapping
    public LancamentoResponseDTO create(@RequestBody LancamentoRequestDTO lancamento){
        return service.createLaunch(lancamento);
    }

    @GetMapping("/{id}")
    public LancamentoResponseDTO findById(@PathVariable("id") Long id){
        return service.findLaunchById(id);
    }

    @GetMapping
    public List<LancamentoResponseDTO> findAll(){
        return service.findAllLaunches();
    }
}
