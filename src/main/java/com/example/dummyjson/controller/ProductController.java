package com.example.dummyjson.controller;

import com.example.dummyjson.dto.Product;
import com.example.dummyjson.dto.ProductResponse;
import com.example.dummyjson.service.ProductService;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotNull;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping
    public Mono<List<Product>> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Mono<Product> getProductById(@PathVariable @NotNull Long id) {
        return productService.getProductById(id);
    }
}
