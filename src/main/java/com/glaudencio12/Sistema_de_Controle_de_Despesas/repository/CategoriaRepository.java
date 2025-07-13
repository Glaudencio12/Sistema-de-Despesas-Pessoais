package com.glaudencio12.Sistema_de_Controle_de_Despesas.repository;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {}
