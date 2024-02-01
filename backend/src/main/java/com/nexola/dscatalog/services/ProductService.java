package com.nexola.dscatalog.services;

import com.nexola.dscatalog.dto.CategoryDTO;
import com.nexola.dscatalog.dto.ProductDTO;
import com.nexola.dscatalog.entities.Category;
import com.nexola.dscatalog.entities.Product;
import com.nexola.dscatalog.projections.ProductProjection;
import com.nexola.dscatalog.repositories.CategoryRepository;
import com.nexola.dscatalog.repositories.ProductRepository;
import com.nexola.dscatalog.services.exceptions.DatabaseException;
import com.nexola.dscatalog.services.exceptions.ResourceNotFoundException;
import com.nexola.dscatalog.util.Utils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Autowired
    CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(String name, String categoryId, Pageable pageable) {
        List<Long> categoryIds = List.of();

        if (!Objects.equals(categoryId, "0")) {
            String[] arr = categoryId.split(",");
            List<String> list = Arrays.asList(arr);
            categoryIds = list.stream().map(Long::parseLong).toList();
        }

        Page<ProductProjection> page = repository.searchProducts(categoryIds, name, pageable);

        List<Long> productIds = page.map(x -> x.getId()).stream().toList();

        List<Product> entities = repository.searchProductsWithCategories(productIds);
        entities = Utils.replace(page.getContent(), entities);

        List<ProductDTO> dtos = entities.stream().map(ProductDTO::new).toList();

        return new PageImpl<>(dtos, page.getPageable(), page.getTotalElements());
    }
    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product prod = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado")
        );
        return new ProductDTO(prod);
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto) {
        Product entity = new Product();
        dtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        try {
            Product entity = repository.getReferenceById(id);
            dtoToEntity(dto, entity);
            entity = repository.save(entity);
            return new ProductDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
        try {
            repository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial");
        }
    }

    public void dtoToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setImgUrl(dto.getImgUrl());
        entity.setDate(dto.getDate());
        for (CategoryDTO catDto : dto.getCategories()) {
            Category category = categoryRepository.getReferenceById(catDto.getId());
            entity.getCategories().add(category);
        }
    }


}
