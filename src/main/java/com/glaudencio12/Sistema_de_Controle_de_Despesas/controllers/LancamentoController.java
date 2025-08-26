package com.glaudencio12.Sistema_de_Controle_de_Despesas.controllers;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.controllers.docs.LancamentoControllerDocs;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.request.LancamentoRequestDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response.LancamentoResponseDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.services.LancamentoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<LancamentoResponseDTO> create(@RequestBody @Valid LancamentoRequestDTO lancamento){
        LancamentoResponseDTO lancamentoResponse = service.createLaunch(lancamento);
        return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoResponse);
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<LancamentoResponseDTO> findById(@PathVariable("id") Long id){
        LancamentoResponseDTO lancamentoResponse = service.findLaunchById(id);
        return ResponseEntity.ok(lancamentoResponse);
    }

    @GetMapping
    @Override
    public ResponseEntity<List<LancamentoResponseDTO>> findAll(){
        List<LancamentoResponseDTO> lancamentosResponse = service.findAllLaunches();
        return ResponseEntity.ok(lancamentosResponse);
    }
}
