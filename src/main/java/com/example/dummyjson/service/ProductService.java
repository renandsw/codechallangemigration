package com.example.dummyjson.service;

import com.example.dummyjson.dto.Product;
import com.example.dummyjson.dto.ProductResponse;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final String BASE_URL = "https://dummyjson.com/products";

    private RestTemplate restTemplate;

    public ProductService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public Optional<List<Product>> getAllProducts() {
        ProductResponse response = restTemplate.getForObject(BASE_URL, ProductResponse.class);
        return Optional.of(response.getProducts());
    }

    public Product getProductById(Long id) {
        String url = BASE_URL + "/" + id;
        return restTemplate.getForObject(url, Product.class);
    }
}
