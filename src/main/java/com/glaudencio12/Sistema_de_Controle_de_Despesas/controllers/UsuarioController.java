package com.glaudencio12.Sistema_de_Controle_de_Despesas.controllers;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.controllers.docs.UsuarioControllerDocs;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.request.UsuarioRequestDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response.UsuarioResponseDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.services.UsuarioService;
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

import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuários", description = "Esta seção reúne os endpoints responsáveis pelas operações de criação, consulta, atualização e exclusão de usuários.")
public class UsuarioController implements UsuarioControllerDocs {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping(
            value = "/createUser",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public ResponseEntity<UsuarioResponseDTO> create(@RequestBody @Valid UsuarioRequestDTO usuario) {
        UsuarioResponseDTO usuarioResponse = service.createUser(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioResponse);
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponseDTO> findById(@PathVariable("id") Long id) {
        UsuarioResponseDTO usuario = service.findUserById(id);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PagedModel<EntityModel<UsuarioResponseDTO>>> findAll(Pageable pageable) {
        PagedModel<EntityModel<UsuarioResponseDTO>> usuarios = service.findAllUsers(pageable);
        return ResponseEntity.ok(usuarios);
    }

    @PutMapping(
            value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<UsuarioResponseDTO> update(@PathVariable("id") Long id, @RequestBody @Valid UsuarioRequestDTO usuarioRequest) {
        UsuarioResponseDTO usuarioResponse = service.updateUserById(id, usuarioRequest);
        return ResponseEntity.ok(usuarioResponse);
    }

    @PatchMapping(
            value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<UsuarioResponseDTO> updatePatch(@PathVariable("id") Long id, @RequestBody Map<String, Object> usuarioRequest) {
        UsuarioResponseDTO usuarioResponse = service.updateSpecificFields(id, usuarioRequest);
        return ResponseEntity.ok(usuarioResponse);
    }


    @DeleteMapping("/{id}")
    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}
