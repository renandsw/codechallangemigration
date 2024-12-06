package com.example.dummyjson.integrationTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.dummyjson.controller.ProductController;
import com.example.dummyjson.dto.Product;
import com.example.dummyjson.dto.ProductResponse;
import com.example.dummyjson.service.ProductService;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Integration class 
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DummyJsonIntegrationTest {
    @LocalServerPort
    private int port;
    private MockWebServer server;
    
    private ProductController productController;
    private ProductService productService;

    @Autowired
    private static WebTestClient webTestClient;

    @BeforeAll
    void setUp() throws IOException{
        server = new MockWebServer();
        server.start();
        
        productService = new ProductService(WebClient.builder());
        productController = new ProductController(productService);
    }

    @AfterEach
    void shutDown() throws IOException{
        if (server!=null){
            server.shutdown();
        }
    }

    @Test
    void testGetAllProducts() {
        String mockResponseBody = """
                [
                    {
                        "id": 1,
                        "title": "First Product",
                        "description": "blabla",
                        "price": 35
                    },
                    {
                        "id": 2,
                        "title": "Second Product",
                        "description": "bla bla again",
                        "price": 45
                    }
                    {
                        "id": 3,
                        "title": "Third Product",
                        "description": "bla bla bla once again",
                        "price": 56
                    }
                ]
                """;

        server.enqueue(new MockResponse().setBody(mockResponseBody).addHeader("Content-Type", "application/json"));

        webTestClient.get().uri("http://localhost:"+port+"/api/products").exchange()
            .expectStatus().isOk().expectBodyList(ProductResponse.class)
            .consumeWith(response -> {
                ProductResponse productResponse = (ProductResponse) response.getResponseBody();
                assertNotNull(productResponse);
                assertEquals(3,productResponse.getProducts().size());
            });
    }



}
