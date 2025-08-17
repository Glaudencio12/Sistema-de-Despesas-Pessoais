package com.glaudencio12.Sistema_de_Controle_de_Despesas.utils;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.controllers.CategoriaController;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.controllers.UsuarioController;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response.CategoriaResponseDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response.LancamentoResponseDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response.UsuarioResponseDTO;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Service
public class HateoasLinks {
    public void links(Object dto){
        if (dto instanceof UsuarioResponseDTO dto1) {
            dto1.add(linkTo(methodOn(UsuarioController.class).findById(dto1.getId())).withRel("FindUserById").withTitle("Busca um usuário pelo seu ID").withType("GET"));
            dto1.add(linkTo(methodOn(UsuarioController.class).findAll()).withRel("FindAllUsers").withTitle("Busca todos os usuários cadastrados").withType("GET"));
            dto1.add(linkTo(methodOn(UsuarioController.class).create(null)).withRel("CreateUser").withTitle("Cria um usuário com dados passados via Body").withType("POST"));
            dto1.add(linkTo(methodOn(UsuarioController.class).update(dto1.getId(), null)).withRel("UpdateUserById").withTitle("Atualiza o usuário pelo seu ID e novos dados passados via body").withType("PUT"));
            dto1.add(linkTo(methodOn(UsuarioController.class).delete(dto1.getId())).withRel("DeleteUser").withTitle("Excluí usuário pelo seu ID").withType("DELETE"));

        } else if (dto instanceof LancamentoResponseDTO dto2){
            dto2.add(linkTo(methodOn(UsuarioController.class).findById(dto2.getId())).withRel("FindLaunchById").withTitle("Busca um lançamento pelo seu ID").withType("GET"));
            dto2.add(linkTo(methodOn(UsuarioController.class).findAll()).withRel("FindAllLaunchs").withTitle("Busca todos os lançamentos realizados").withType("GET"));
            dto2.add(linkTo(methodOn(UsuarioController.class).create(null)).withRel("CreateLaunch").withTitle("Cadastra um novo lançamento com os dados passados via Body").withType("POST"));

        }else if (dto instanceof CategoriaResponseDTO dto3){
            dto3.add(linkTo(methodOn(CategoriaController.class).findById(dto3.getId())).withRel("FindCategoryById").withTitle("Busca uma categoria por id").withType("GET"));
            dto3.add(linkTo(methodOn(CategoriaController.class).findAll()).withRel("FindAllCategorys").withTitle("Busca todas as categoria adicionadas").withType("GET"));
            dto3.add(linkTo(methodOn(CategoriaController.class).create(null)).withRel("CreateCategory").withTitle("Adiciona uma nova categoria").withType("POST"));
            dto3.add(linkTo(methodOn(CategoriaController.class).delete(dto3.getNome())).withRel("DeleteCategory").withTitle("Excluí uma categoria pelo seu nome").withType("DELETE"));
        }
    }
}

