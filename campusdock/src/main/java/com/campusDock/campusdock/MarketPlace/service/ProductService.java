package com.campusDock.campusdock.MarketPlace.service;

import com.campusDock.campusdock.MarketPlace.entity.Product;
import com.campusDock.campusdock.MarketPlace.dto.ProductCreateDto;
import com.campusDock.campusdock.MarketPlace.dto.ProductDetailDto;
import com.campusDock.campusdock.dto.ProductDto;
import com.campusDock.campusdock.entity.MediaFile;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    // 1. Post item
    ProductDetailDto createProduct(
            ProductCreateDto productDto,
            List<MultipartFile> productFiles
    );

    // 2. Get All Products
    Page<ProductDto> getAllProducts(int page, int size, String sortField, String sortDir, UUID collegeId, UUID userId);

    ProductDetailDto getProductDetail(UUID id);

    Product updateProduct(UUID id, ProductCreateDto productDto);

    void deleteProduct(UUID id);
}
