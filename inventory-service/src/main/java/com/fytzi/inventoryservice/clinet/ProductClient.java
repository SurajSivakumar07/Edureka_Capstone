package com.fytzi.inventoryservice.clinet;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "PRODUCT-SERVICE")  // must match spring.application.name of product service
public interface ProductClient {

    @GetMapping("/products/{id}")
    void validateProductExists(@PathVariable("id") Long productId);
}
