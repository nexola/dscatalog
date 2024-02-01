package com.nexola.dscatalog.services;

import com.nexola.dscatalog.dto.ProductDTO;
import com.nexola.dscatalog.entities.Product;
import com.nexola.dscatalog.factories.ProductFactory;
import com.nexola.dscatalog.repositories.ProductRepository;
import com.nexola.dscatalog.services.exceptions.DatabaseException;
import com.nexola.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;
    private PageImpl<Product> page;
    private Product product;
    private ProductDTO productDTO;
    private ProductDTO updatedProductDTO;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;
        product = ProductFactory.createProduct();
        page = new PageImpl<>(List.of(product));
        productDTO = ProductFactory.createProductDTO();
        updatedProductDTO = ProductFactory.createUpdatedProductDTO();
        // findAll
        Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);
        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);
        // findById
        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
        // update
        Mockito.when(repository.getReferenceById(existingId)).thenReturn(product);
        Mockito.doThrow(ResourceNotFoundException.class).when(repository).getReferenceById(nonExistingId);
        // delete
        Mockito.doNothing().when(repository).deleteById(existingId);
        Mockito.doThrow(DatabaseException.class).when(repository).deleteById(dependentId);
        Mockito.when(repository.existsById(existingId)).thenReturn(true);
        Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);
        Mockito.when(repository.existsById(dependentId)).thenReturn(true);
    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdExists() {
        productDTO = service.findById(existingId);
        Assertions.assertNotNull(productDTO);
        Mockito.verify(repository, Mockito.times(1)).findById(existingId);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundWhenIdDoesNotExists() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(nonExistingId);
        });
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists() {
        productDTO = service.update(existingId, updatedProductDTO);
        Assertions.assertNotNull(productDTO);
        Mockito.verify(repository, Mockito.times(1)).save(product);
    }

    @Test
    public void updateShouldThrowResourceNotFoundWhenIdDoesNotExists() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.update(nonExistingId, updatedProductDTO);
        });
    }


    @Test
    public void findAllShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductDTO> result = service.findAll("", "", pageable);

        Assertions.assertNotNull(result);
        Mockito.verify(repository, Mockito.times(1)).findAll(pageable);
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

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingId);
        });
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
        Assertions.assertThrows(DatabaseException.class, () -> {
            service.delete(dependentId);
        });
    }

}
