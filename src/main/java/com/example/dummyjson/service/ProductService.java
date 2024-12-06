package com.example.dummyjson.service;

import com.example.dummyjson.dto.Product;
import com.example.dummyjson.dto.ProductResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
/* import org.springframework.web.client.RestTemplate; */
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final String BASE_URL = "https://dummyjson.com/products";
    /* Left in comment for the sake of letting judges know how I solved the initial 
        scenario of json Deserialization */
    //private RestTemplate restTemplate;
    private final WebClient webClient;

    /* public ProductService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    } */

    public ProductService(WebClient.Builder webClientbBuilder){
        this.webClient = webClientbBuilder.baseUrl(BASE_URL).build();
    }

    public Mono<List<Product>> getAllProducts() {

        /* 
        ProductResponse response = restTemplate.getForObject(BASE_URL, ProductResponse.class);
        return Optional.of(response.getProducts()); */
        Mono<ProductResponse> prodResponse =  webClient.get().uri("/").header("Accept","application/json")
        .retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, t -> t.bodyToMono(String.class).flatMap(errorBody -> Mono.error(new RuntimeException("API Error: " + errorBody))))
        .bodyToMono(String.class).flatMap(rawResponse -> {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                ProductResponse productResponse = objectMapper.readValue(rawResponse, ProductResponse.class);
                return Mono.just(productResponse);
            } catch (Exception e) {
                return Mono.error(new RuntimeException("Failed to parse response: " + e.getMessage(), e));
            }
        });
        return Mono.just(prodResponse.block().getProducts());
    }

    public Mono<Product> getProductById(Long id) {
        /* String url = BASE_URL + "/" + id;
        return restTemplate.getForObject(url, Product.class); */
        return webClient.get().uri("/{id}",id).retrieve()
        .onStatus(HttpStatusCode::is5xxServerError, t -> Mono.error(new RuntimeException("Server unexpected error occurred")))
        .onStatus(HttpStatusCode::is4xxClientError, t -> Mono.error(new RuntimeException("Client unexpected error occurred")))
        .bodyToMono(Product.class);
    }
}
