package com.glaudencio12.Sistema_de_Controle_de_Despesas.controllers;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.request.UsuarioRequestDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response.UsuarioResponseDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/controle-de-despesas")
public class UsuarioController {
    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping("/register-user")
    public UsuarioResponseDTO create(@RequestBody @Valid UsuarioRequestDTO usuario){
        return service.createUser(usuario);
    }

    @GetMapping("/find-user/{id}")
    public UsuarioResponseDTO findById(@PathVariable("id") Long id){
        return service.findUserById(id);
    }

    @GetMapping("/findAll-users")
    public List<UsuarioResponseDTO> findAll(){
        return service.findAllUsers();
    }

    @PutMapping("/update-user/{id}")
    public UsuarioResponseDTO update(@PathVariable("id") Long id, @RequestBody @Valid UsuarioRequestDTO usuarioRequest){
        return service.updateUserById(id, usuarioRequest);
    }

    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        service.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}
