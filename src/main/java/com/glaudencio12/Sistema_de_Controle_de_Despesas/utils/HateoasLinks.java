package com.glaudencio12.Sistema_de_Controle_de_Despesas.utils;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.controllers.UsuarioController;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.request.UsuarioRequestDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response.UsuarioResponseDTO;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Service
public class HateoasLinks {
    public void links(Object dto){
        if (dto instanceof UsuarioResponseDTO dto1) {
            UsuarioRequestDTO requestDTO = new UsuarioRequestDTO();

            dto1.add(linkTo(methodOn(UsuarioController.class).findById(dto1.getId())).withRel("findUserById").withTitle("Busca um usuário pelo seu ID").withType("GET"));
            dto1.add(linkTo(methodOn(UsuarioController.class).findAll()).withRel("findAllUsers").withTitle("Busca todos os usuários cadastrados").withType("GET"));
            dto1.add(linkTo(methodOn(UsuarioController.class).create(requestDTO)).withRel("CreateUser").withTitle("Cria um usuário com dados passados via Body").withType("POST"));
            dto1.add(linkTo(methodOn(UsuarioController.class).update(dto1.getId(), requestDTO)).withRel("UpdateUserById").withTitle("Atualiza o usuário pelo seu ID e novos dados passados via body").withType("PUT"));
            dto1.add(linkTo(methodOn(UsuarioController.class).delete(dto1.getId())).withRel("DeleteUser").withTitle("Excluí usuário pelo seu ID").withType("DELETE"));
        }
    }
}
