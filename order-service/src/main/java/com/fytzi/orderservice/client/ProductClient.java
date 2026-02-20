package com.fytzi.orderservice.client;

import com.fytzi.orderservice.dto.CreateOrderRequest;
import com.fytzi.orderservice.dto.ProductListRequest;
import com.fytzi.orderservice.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "PRODUCT-SERVICE")
public interface ProductClient {

    @PostMapping("/products/validation")
    ResponseEntity<Boolean> checkProductExsists(@RequestBody ProductListRequest prodList);

    @PostMapping("/products/reduceStock")
    Boolean placeOrder(@RequestBody CreateOrderRequest createOrderItemRequest);

    @PostMapping("/products/details")
    ResponseEntity<List<ProductDto>> getProductsDetails(@RequestBody ProductListRequest prodList);
}
