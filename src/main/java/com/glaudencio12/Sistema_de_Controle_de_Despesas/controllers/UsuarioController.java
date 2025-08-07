package com.glaudencio12.Sistema_de_Controle_de_Despesas.controllers;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.controllers.docs.UsuarioControllerDocs;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.request.UsuarioRequestDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response.UsuarioResponseDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.services.UsuarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuários", description = "Esta seção reúne os endpoints responsáveis pelas operações de criação, consulta, atualização e exclusão de usuários.")
public class UsuarioController implements UsuarioControllerDocs {
    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping
    @Override
    public UsuarioResponseDTO create(@RequestBody @Valid UsuarioRequestDTO usuario){
        return service.createUser(usuario);
    }

    @GetMapping("/{id}")
    @Override
    public UsuarioResponseDTO findById(@PathVariable("id") Long id){
        return service.findUserById(id);
    }

    @GetMapping
    @Override
    public List<UsuarioResponseDTO> findAll(){
        return service.findAllUsers();
    }

    @PutMapping("/{id}")
    @Override
    public UsuarioResponseDTO update(@PathVariable("id") Long id, @RequestBody @Valid UsuarioRequestDTO usuarioRequest){
        return service.updateUserById(id, usuarioRequest);
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        service.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}
