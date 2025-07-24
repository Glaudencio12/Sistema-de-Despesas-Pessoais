package com.glaudencio12.Sistema_de_Controle_de_Despesas.controllers;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.request.CategoriaRequestDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response.CategoriaResponseDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.services.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/controle-de-despesas")
public class CategoriaController {
    private final CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    @PostMapping("/create-category")
    public CategoriaResponseDTO create(@RequestBody @Valid CategoriaRequestDTO categoriaRequest){
        return service.createCategory(categoriaRequest);
    }

    @GetMapping("/find-category/{id}")
    public CategoriaResponseDTO findById(@PathVariable("id") Long id){
        return service.findCatgeoryById(id);
    }

    @GetMapping("/findAll-categorys")
    public List<CategoriaResponseDTO> findAll(){
        return service.findAllCatgorys();
    }

    @DeleteMapping("/delete-category/{nomeCategoria}")
    public ResponseEntity<?> delete(@PathVariable("nomeCategoria") String nomeCategoria){
        service.deleteCategoryByName(nomeCategoria);
        return ResponseEntity.noContent().build();
    }
}
