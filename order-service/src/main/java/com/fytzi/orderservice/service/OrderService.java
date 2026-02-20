package com.fytzi.orderservice.service;

import com.fytzi.orderservice.dto.CreateOrderRequest;
import com.fytzi.orderservice.dto.OrderResponseDto;

import java.util.List;

public interface OrderService {

    OrderResponseDto createOrder(CreateOrderRequest request,String userId);

    List<OrderResponseDto> getOrdersByUser(Long userId);
}
