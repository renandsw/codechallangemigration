package com.example.dummyjson.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Wrapper class to uphold Product class and Deserialize it
 */
public class ProductResponse {
    @JsonProperty("products")
    private List<Product> products;
    
    public List<Product> getProducts(){
        return products;
    }

    public void setProducts(List<Product> productsList){
        this.products = productsList;
    }
}
