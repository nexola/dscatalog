package com.nexola.dscatalog.services;

import com.nexola.dscatalog.repositories.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    private Long existingId;
    private long nonExistingId;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;
        // Simula o comportamento do repository (não deve retornar nada quando o método for chamado com ID existente)
        Mockito.doNothing().when(repository).deleteById(existingId);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        // Não deve lançar exceção quando o método for chamado (service)
        Assertions.assertDoesNotThrow(() -> {
            service.delete(existingId);
        });
        // Verifica se o método do repository foi chamado no delete do service (Mockito.times) vezes
        Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);

    }
}
