package com.glaudencio12.Sistema_de_Controle_de_Despesas.controllers;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.controllers.docs.CategoriaControllerDocs;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.request.CategoriaRequestDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response.CategoriaResponseDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.services.CategoriaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@Tag(name = "Categorias", description = "Esta seção reúne os endpoints responsáveis pelas operações de criação, consulta e exclusão de categorias.")
public class CategoriaController implements CategoriaControllerDocs {
    private final CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    @PostMapping
    @Override
    public CategoriaResponseDTO create(@RequestBody @Valid CategoriaRequestDTO categoriaRequest){
        return service.createCategory(categoriaRequest);
    }

    @GetMapping("/{id}")
    @Override
    public CategoriaResponseDTO findById(@PathVariable("id") Long id){
        return service.findCatgeoryById(id);
    }

    @GetMapping
    @Override
    public List<CategoriaResponseDTO> findAll(){
        return service.findAllCatgorys();
    }

    @DeleteMapping("/{nomeCategoria}")
    @Override
    public ResponseEntity<?> delete(@PathVariable("nomeCategoria") String nomeCategoria){
        service.deleteCategoryByName(nomeCategoria);
        return ResponseEntity.noContent().build();
    }
}
