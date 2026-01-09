package com.glaudencio12.Sistema_de_Controle_de_Despesas.utils;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.controllers.CategoriaController;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.controllers.LancamentoController;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.controllers.UsuarioController;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response.CategoriaResponseDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response.LancamentoResponseDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response.UsuarioResponseDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class HateoasLinks {
    public void links(Object dto) {

        if (dto instanceof UsuarioResponseDTO usuario) {
            addUsuarioLinks(usuario);

        } else if (dto instanceof LancamentoResponseDTO lancamento) {
            addLancamentoLinks(lancamento);

        } else if (dto instanceof CategoriaResponseDTO categoria) {
            addCategoriaLinks(categoria);
        }
    }
    
    private void addUsuarioLinks(UsuarioResponseDTO dto) {
        dto.add(linkTo(methodOn(UsuarioController.class)
                .findById(dto.getId()))
                .withRel("findUserById")
                .withType("GET")
                .withTitle("Busca um usuário pelo seu ID"));

        dto.add(linkTo(methodOn(UsuarioController.class)
                .findAll(null))
                .withRel("findAllUsers")
                .withType("GET")
                .withTitle("Busca todos os usuários cadastrados"));

        dto.add(linkTo(methodOn(UsuarioController.class)
                .create(null))
                .withRel("createUser")
                .withType("POST")
                .withTitle("Cria um usuário com dados passados via body"));

        dto.add(linkTo(methodOn(UsuarioController.class)
                .update(dto.getId(), null))
                .withRel("updateUserById")
                .withType("PUT")
                .withTitle("Atualiza o usuário pelo ID"));

        dto.add(linkTo(methodOn(UsuarioController.class)
                .delete(dto.getId()))
                .withRel("deleteUserById")
                .withType("DELETE")
                .withTitle("Exclui usuário pelo ID"));
    }
    
    private void addLancamentoLinks(LancamentoResponseDTO dto) {
        dto.add(linkTo(methodOn(LancamentoController.class)
                .findById(dto.getId()))
                .withRel("findLaunchById")
                .withType("GET")
                .withTitle("Busca um lançamento pelo seu ID"));

        dto.add(linkTo(methodOn(LancamentoController.class)
                .findAll(null))
                .withRel("findAllLaunches")
                .withType("GET")
                .withTitle("Busca todos os lançamentos realizados"));

        dto.add(linkTo(methodOn(LancamentoController.class)
                .create(null))
                .withRel("createLaunch")
                .withType("POST")
                .withTitle("Cadastra um novo lançamento"));
    }
    
    private void addCategoriaLinks(CategoriaResponseDTO dto) {
        dto.add(linkTo(methodOn(CategoriaController.class)
                .findById(dto.getId()))
                .withRel("findCategoryById")
                .withType("GET")
                .withTitle("Busca uma categoria pelo ID"));

        dto.add(linkTo(methodOn(CategoriaController.class)
                .findAll(null))
                .withRel("findAllCategories")
                .withType("GET")
                .withTitle("Busca todas as categorias"));

        dto.add(linkTo(methodOn(CategoriaController.class)
                .create(null))
                .withRel("createCategory")
                .withType("POST")
                .withTitle("Adiciona uma nova categoria"));

        dto.add(linkTo(methodOn(CategoriaController.class)
                .delete(dto.getNome()))
                .withRel("deleteCategoryByName")
                .withType("DELETE")
                .withTitle("Exclui uma categoria pelo nome"));
    }
}
