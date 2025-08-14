package com.campusDock.campusdock.service;

import com.campusDock.campusdock.dto.ProductCreateDto;
import com.campusDock.campusdock.dto.ProductDetailDto;
import com.campusDock.campusdock.dto.ProductDto;
import com.campusDock.campusdock.entity.Product;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface ProductService {

    // 1. Post item
    ProductDetailDto createProduct(ProductCreateDto productDto);

    // 2. Get All Products
    Page<ProductDto> getAllProducts(int page, int size, String sortField, String sortDir, UUID collegeId, UUID userId);

    ProductDetailDto getProductDetail(UUID id);

    Product updateProduct(UUID id, ProductCreateDto productDto);

    void deleteProduct(UUID id);
}
