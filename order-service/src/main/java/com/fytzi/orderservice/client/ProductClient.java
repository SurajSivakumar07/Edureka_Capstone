package com.fytzi.orderservice.client;

import com.fytzi.orderservice.dto.CreateOrderRequest;
import com.fytzi.orderservice.dto.ProductListRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "PRODUCT-SERVICE")
public interface ProductClient {

    @PostMapping("/products/validation")
    Boolean checkProductExsists(@RequestBody ProductListRequest prodList);

    @PostMapping("/products/reduceStock")
    Boolean placeOrder(@RequestBody CreateOrderRequest createOrderItemRequest);
}

