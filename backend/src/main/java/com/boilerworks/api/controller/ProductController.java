package com.boilerworks.api.controller;

import com.boilerworks.api.dto.*;
import com.boilerworks.api.model.Product;
import com.boilerworks.api.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @PreAuthorize("hasAuthority('product.view')")
    public List<ProductDto> list(@RequestParam(required = false) String search) {
        return productService.findAll(search);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('product.view')")
    public ProductDto get(@PathVariable UUID id) {
        return productService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('product.add')")
    public ApiResponse<ProductDto> create(@Valid @RequestBody CreateProductRequest request) {
        Product product = productService.create(request);
        return ApiResponse.ok(ProductDto.from(product));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('product.change')")
    public ApiResponse<ProductDto> update(@PathVariable UUID id,
                                           @Valid @RequestBody UpdateProductRequest request) {
        Product product = productService.update(id, request);
        return ApiResponse.ok(ProductDto.from(product));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('product.delete')")
    public ApiResponse<Void> delete(@PathVariable UUID id) {
        productService.softDelete(id);
        return ApiResponse.ok(null);
    }
}
