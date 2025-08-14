package com.campusDock.campusdock.controller;

import com.campusDock.campusdock.dto.ProductCreateDto;
import com.campusDock.campusdock.dto.ProductDetailDto;
import com.campusDock.campusdock.dto.ProductDto;
import com.campusDock.campusdock.entity.Product;
import com.campusDock.campusdock.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/marketplace")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 1. Post item
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductCreateDto productDto) {
        try {
            ProductDetailDto response = productService.createProduct(productDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Something went wrong: " + e.getMessage()));
        }
    }


    // 2. Get all
    @GetMapping
    public ResponseEntity<?> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "listedOn") String sortField,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) UUID collegeId,
            @RequestParam(required = false) UUID userId
    ) {
        try {
            Page<ProductDto> response = productService.getAllProducts(page, size, sortField, sortDir, collegeId, userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Could not fetch products"));
        }
    }

    // 3. Get a Single Product
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductDetail(@PathVariable UUID id) {
        try {
            ProductDetailDto result = productService.getProductDetail(id);
            return ResponseEntity.ok(result);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Could not fetch product details"));
        }
    }


    // 4. Update Product
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable UUID id, @RequestBody ProductCreateDto productDto) {
        try {
            Product updated = productService.updateProduct(id, productDto);
            return ResponseEntity.ok(updated);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Could not update product"));
        }
    }

    // 5. Delete Product... delete -- auto archive after 60-90 days ??
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable UUID id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Could not delete product"));
        }
    }


////    / POST /api/v1/products
//
//
//    // GET /api/v1/products?collegeId=...&page=...
//    @GetMapping
//    public List<ProductDto> getAllProducts(
//            @RequestParam(required = false) UUID collegeId,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size
//    ) {
//        // ... business logic to fetch products with filters
//        return productList;
//    }
//
//    // GET /api/v1/products/{id}
//    @GetMapping("/{id}")
//    public ProductDto getProductById(@PathVariable UUID id) {
//        // ... business logic to find a single product
//        return product;
//    }
//
//    // PATCH /api/v1/products/{id}
//    @PatchMapping("/{id}")
//    public ProductDto updateProduct(@PathVariable UUID id, @RequestBody ProductUpdateDto updateDto) {
//        // ... business logic to update a product
//        return updatedProduct;
//    }
//
//    // DELETE /api/v1/products/{id}
//    @DeleteMapping("/{id}")
//    public void deleteProduct(@PathVariable UUID id) {
//        // ... business logic to delete a product
//    }
//
//    // Optional: Get all products for a specific user
//    // This is often a more useful endpoint for a user's dashboard
//    @GetMapping("/user/{userId}")
//    public List<ProductDto> getProductsByUser(@PathVariable UUID userId) {
//        // ... fetch products listed by a specific user
//        return products;
//    }

}
