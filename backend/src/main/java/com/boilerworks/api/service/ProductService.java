package com.boilerworks.api.service;

import com.boilerworks.api.dto.*;
import com.boilerworks.api.model.Category;
import com.boilerworks.api.model.Product;
import com.boilerworks.api.repository.CategoryRepository;
import com.boilerworks.api.repository.ProductRepository;
import com.boilerworks.api.security.BoilerworksUserDetails;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;

  @Transactional(readOnly = true)
  public List<ProductDto> findAll(String search) {
    List<Product> products;
    if (search != null && !search.isBlank()) {
      products = productRepository.search(search);
    } else {
      products = productRepository.findAll();
    }
    return products.stream().map(ProductDto::from).toList();
  }

  @Transactional(readOnly = true)
  public ProductDto findById(UUID id) {
    Product product =
        productRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    return ProductDto.from(product);
  }

  public Product create(CreateProductRequest request) {
    if (productRepository.existsBySku(request.getSku())) {
      throw new IllegalArgumentException("SKU already exists");
    }

    Product product = new Product();
    product.setName(request.getName());
    product.setSlug(SlugUtil.slugify(request.getName()));
    product.setDescription(request.getDescription());
    product.setPrice(request.getPrice());
    product.setSku(request.getSku());
    product.setActive(request.isActive());

    if (request.getCategoryId() != null) {
      Category category =
          categoryRepository
              .findById(request.getCategoryId())
              .orElseThrow(() -> new EntityNotFoundException("Category not found"));
      product.setCategory(category);
    }

    return productRepository.save(product);
  }

  public Product update(UUID id, UpdateProductRequest request) {
    Product product =
        productRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Product not found"));

    if (request.getName() != null) {
      product.setName(request.getName());
      product.setSlug(SlugUtil.slugify(request.getName()));
    }
    if (request.getDescription() != null) {
      product.setDescription(request.getDescription());
    }
    if (request.getPrice() != null) {
      product.setPrice(request.getPrice());
    }
    if (request.getSku() != null) {
      product.setSku(request.getSku());
    }
    if (request.getActive() != null) {
      product.setActive(request.getActive());
    }
    if (request.getCategoryId() != null) {
      Category category =
          categoryRepository
              .findById(request.getCategoryId())
              .orElseThrow(() -> new EntityNotFoundException("Category not found"));
      product.setCategory(category);
    }

    return productRepository.save(product);
  }

  public void softDelete(UUID id) {
    Product product =
        productRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Product not found"));

    UUID userId = getCurrentUserId();
    product.softDelete(userId);
    productRepository.save(product);
  }

  private UUID getCurrentUserId() {
    var auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null && auth.getPrincipal() instanceof BoilerworksUserDetails userDetails) {
      return userDetails.getUserId();
    }
    return null;
  }
}
