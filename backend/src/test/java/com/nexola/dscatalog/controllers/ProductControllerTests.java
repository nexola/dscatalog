package com.nexola.dscatalog.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexola.dscatalog.dto.ProductDTO;
import com.nexola.dscatalog.factories.ProductFactory;
import com.nexola.dscatalog.services.ProductService;
import com.nexola.dscatalog.services.exceptions.DatabaseException;
import com.nexola.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    @Autowired
    private ObjectMapper mapper;

    private ProductDTO productDTO;
    private PageImpl<ProductDTO> page;
    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;

    @BeforeEach
    void setUp() throws Exception {
        productDTO = ProductFactory.createProductDTO();
        page = new PageImpl<>(List.of(productDTO));
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;

        // findAll
        Mockito.when(service.findAll(ArgumentMatchers.any())).thenReturn(page);
        // findById
        Mockito.when(service.findById(existingId)).thenReturn(productDTO);
        Mockito.when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
        // update
        Mockito.when(service.update(eq(existingId), ArgumentMatchers.any())).thenReturn(productDTO);
        Mockito.when(service.update(eq(nonExistingId), ArgumentMatchers.any())).thenThrow(ResourceNotFoundException.class);
        // delete
        Mockito.doNothing().when(service).delete(existingId);
        Mockito.doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
        Mockito.doThrow(DatabaseException.class).when(service).delete(dependentId);
        // insert
        Mockito.when(service.insert(ArgumentMatchers.any())).thenReturn(productDTO);
    }

    @Test
    public void findAllShouldReturnPage() throws Exception {
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.get("/products")
                        .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnProductWhenIdExists() throws Exception {
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.get("/products/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.get("/products/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists() throws Exception {

        String jsonBody = mapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.put("/products/{id}", existingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        String jsonBody = mapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.put("/products/{id}", nonExistingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.delete("/products/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNoContent());
        result.andExpect(jsonPath("$.id").doesNotExist());
        result.andExpect(jsonPath("$.name").doesNotExist());
        result.andExpect(jsonPath("$.description").doesNotExist());
    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdDoesNotExists() throws Exception{
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.delete("/products/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());
    }

    @Test
    public void insertShouldReturnProductDTO() throws Exception {
        String jsonBody = mapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.post("/products")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }

}
