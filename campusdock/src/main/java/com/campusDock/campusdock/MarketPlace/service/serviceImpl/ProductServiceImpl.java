package com.campusDock.campusdock.MarketPlace.service.serviceImpl;

import com.campusDock.campusdock.MarketPlace.dto.MediaDetailsDto;
import com.campusDock.campusdock.MarketPlace.dto.ProductCreateDto;
import com.campusDock.campusdock.MarketPlace.dto.ProductDetailDto;
import com.campusDock.campusdock.MarketPlace.entity.Product;
import com.campusDock.campusdock.MarketPlace.service.ProductService;
import com.campusDock.campusdock.MarketPlace.dto.ProductDto;
import com.campusDock.campusdock.entity.College;
import com.campusDock.campusdock.entity.MediaFile;
import com.campusDock.campusdock.entity.User;
import com.campusDock.campusdock.repository.CollegeRepo;
import com.campusDock.campusdock.repository.ProductRepo;
import com.campusDock.campusdock.repository.UserRepo;
import com.campusDock.campusdock.repository.specs.ProductSpecification;
import com.campusDock.campusdock.service.ServiceImpl.MediaFileServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final CollegeRepo collegeRepo;
    private final UserRepo userRepo;
    private final MediaFileServiceImpl mediaFileServiceImpl;

    public ProductServiceImpl(
            ProductRepo productRepo,
            CollegeRepo collegeRepo,
            UserRepo userRepo,
            MediaFileServiceImpl mediaFileServiceImpl


    ) {
        this.productRepo = productRepo;
        this.collegeRepo = collegeRepo;
        this.userRepo = userRepo;
        this.mediaFileServiceImpl = mediaFileServiceImpl;

    }

    // 1. Post item
    @Override
    public ProductDetailDto createProduct(
            ProductCreateDto productDto,
            List<MultipartFile> productFiles
    ) {
        User user = userRepo.findById(productDto.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User with id:- " + productDto.getUserId() + " not found"));

        College college = collegeRepo.findById(productDto.getCollegeId())
                .orElseThrow(() -> new NoSuchElementException("College with id:- " + productDto.getCollegeId() + " not found"));

        if (productDto.getPrice() == null || productDto.getPrice().doubleValue() <= 0) {
            throw new IllegalArgumentException("Invalid product price");
        }

        Product product = Product.builder()
                .name(productDto.getName())
                .price(productDto.getPrice())
                .description(productDto.getDescription())
                .listedOn(LocalDateTime.now())
                .isServie(productDto.isService())
                .user(user)
                .college(college)
                .build();

        Product savedProduct = productRepo.save(product);

        //  Process media files (NO save inside uploadMedia!)
        if (productFiles != null && !productFiles.isEmpty()) {
            for (MultipartFile file : productFiles) {
                if (file != null && !file.isEmpty()) {
                    try {
                        MediaFile mediaFile = mediaFileServiceImpl.uploadMedia(file);

                        //  set both sides of relationship
                        mediaFile.setProduct(product);
                        mediaFileServiceImpl.save(mediaFile);
                        product.getMediaFiles().add(mediaFile);

                    } catch (Exception e) {
                        System.err.println("Media upload failed: " + e.getMessage());
                    }
                }
            }
        }

        //  only save product (cascade saves media too)


        List<MediaDetailsDto> uploadedMediaList = new ArrayList<>();
        for (MediaFile mediaFile : savedProduct.getMediaFiles()) {
            uploadedMediaList.add(new MediaDetailsDto(mediaFile.getId(), mediaFile.getUrl()));
        }

        return ProductDetailDto.builder()
                .id(savedProduct.getId())
                .name(savedProduct.getName())
                .description(savedProduct.getDescription())
                .price(savedProduct.getPrice())
                .listedOn(savedProduct.getListedOn())
                .isServie(savedProduct.isServie())
                .userName(user.getName())
                .mediaFiles(uploadedMediaList)
                .build();
    }


    // 2. Get All products
    @Override
    public Page<ProductDto> getAllProducts(int page, int size, String sortField, String sortDir, UUID collegeId, UUID userId) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Product> spec = ProductSpecification.withFilters(collegeId, userId);

        Page<Product> products = productRepo.findAll(spec, pageable);
        return products.map(this::convertToDto);
    }

    // 3. Get Product Details
    @Override
    public ProductDetailDto getProductDetail(UUID id) {
        Optional<Product> foundProduct = productRepo.findById(id);
        if (foundProduct.isEmpty()) {
            throw new RuntimeException("Product with id:- " + id + "not found");
        }
        Product product = foundProduct.get();
        ProductDetailDto productDetailDto = ProductDetailDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .listedOn(product.getListedOn())
                .isServie(product.isServie())
                .userName(product.getUser().getName())
                .build();
        return productDetailDto;
    }

    // 4. Update
    @Override
    public Product updateProduct(UUID id, ProductCreateDto productDto) {
        Product existing = productRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product with id: " + id + " not found"));

        // Optionally validate user/college like in createProduct
        if (productDto.getUserId() != null) {
            User user = userRepo.findById(productDto.getUserId())
                    .orElseThrow(() -> new NoSuchElementException("User with id: " + productDto.getUserId() + " not found"));
            existing.setUser(user);
        }

        if (productDto.getCollegeId() != null) {
            College college = collegeRepo.findById(productDto.getCollegeId())
                    .orElseThrow(() -> new NoSuchElementException("College with id: " + productDto.getCollegeId() + " not found"));
            existing.setCollege(college);
        }

        existing.setName(productDto.getName());
        existing.setPrice(productDto.getPrice());
        existing.setDescription(productDto.getDescription());
        existing.setServie(productDto.isService());

        return productRepo.save(existing);
    }

    // 5. Delete
    @Override
    public void deleteProduct(UUID id) {
        Product existing = productRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product with id: " + id + " not found"));
        productRepo.delete(existing);
    }

    // Other
    public ProductDto convertToDto(Product product) {

        List<String> urls = new ArrayList<>();

       //Loop through the media files and add each URL to the list
        if (product.getMediaFiles() != null) {
            for (MediaFile mediaFile : product.getMediaFiles()) {
                urls.add(mediaFile.getUrl());
            }
        }


        return new ProductDto(
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getListedOn(),
                product.getUser().getName(),
                product.getUser().getId(),
                urls // ⚠️
        );
    }


}
