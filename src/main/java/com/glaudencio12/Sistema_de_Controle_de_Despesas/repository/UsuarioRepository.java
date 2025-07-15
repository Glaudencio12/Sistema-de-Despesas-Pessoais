package com.glaudencio12.Sistema_de_Controle_de_Despesas.repository;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByEmail(String email);
}
