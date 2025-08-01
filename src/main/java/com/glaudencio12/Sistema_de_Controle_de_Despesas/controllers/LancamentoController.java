package com.glaudencio12.Sistema_de_Controle_de_Despesas.controllers;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.request.LancamentoRequestDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response.LancamentoResponseDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.services.LancamentoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lancamentos")
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
}
