package com.nexola.dscatalog.factories;

import com.nexola.dscatalog.dto.ProductDTO;
import com.nexola.dscatalog.entities.Category;
import com.nexola.dscatalog.entities.Product;

import java.time.Instant;

public class ProductFactory {

    public static Product createProduct() {
        Product product = new Product(1L, "Phone", "Good phone", 800.0, "https://img.com/img.png", Instant.parse("2020-10-10T00:00:00Z"));
        product.getCategories().add(new Category(2L, "Electronics"));
        return product;
    }

    public static ProductDTO createProductDTO() {
        return new ProductDTO(createProduct());
    }

    public static ProductDTO createUpdatedProductDTO() {
        return new ProductDTO(2L, "Test", "Description", 50.0, "url", Instant.parse("2000-01-01T01:01:01Z"));
    }
}
