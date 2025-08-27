package com.glaudencio12.Sistema_de_Controle_de_Despesas.controllers;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.controllers.docs.CategoriaControllerDocs;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.request.CategoriaRequestDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response.CategoriaResponseDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.services.CategoriaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @PostMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public ResponseEntity<CategoriaResponseDTO> create(@RequestBody @Valid CategoriaRequestDTO categoriaRequest){
        CategoriaResponseDTO categoriaResponse = service.createCategory(categoriaRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaResponse);
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ResponseEntity<CategoriaResponseDTO> findById(@PathVariable("id") Long id){
        CategoriaResponseDTO categoriaResponse = service.findCatgeoryById(id);
        return ResponseEntity.ok(categoriaResponse);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ResponseEntity<List<CategoriaResponseDTO>> findAll(){
        List<CategoriaResponseDTO> categoriasResponse = service.findAllCatgorys();
        return ResponseEntity.ok(categoriasResponse);
    }

    @DeleteMapping("/{nomeCategoria}")
    @Override
    public ResponseEntity<Void> delete(@PathVariable("nomeCategoria") String nomeCategoria){
        service.deleteCategoryByName(nomeCategoria);
        return ResponseEntity.noContent().build();
    }
}
