package com.glaudencio12.Sistema_de_Controle_de_Despesas.services;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class UsuarioServiceTest {

    @Mock
    UsuarioRepository repository;

    @InjectMocks
    UsuarioService service;

    @BeforeEach
    void setUp() {
    }

    @Test
    void createUser() {
    }

    @Test
    void findUserById() {
    }

    @Test
    void findAllUsers() {
    }

    @Test
    void updateUserById() {
    }

    @Test
    void deleteUserById() {
    }
}