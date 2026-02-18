package com.fytzi.orderservice.service.impl;

import com.fytzi.orderservice.client.InventoryClient;
import com.fytzi.orderservice.client.ProductClient;
import com.fytzi.orderservice.dto.*;
import com.fytzi.orderservice.entity.*;
import com.fytzi.orderservice.repository.OrderRepository;
import com.fytzi.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final InventoryClient inventoryClient;

    @Override
    public OrderResponseDto createOrder(CreateOrderRequest request) {

        BigDecimal total = BigDecimal.ZERO;

        Order order = Order.builder()
                .userId(request.userId())
                .status("CREATED")
                .createdAt(LocalDateTime.now())
                .build();

        List<OrderItem> items = request.items().stream().map(item -> {
            var product = productClient.getProduct(item.productId());
            inventoryClient.reserve(new InventoryReserveRequest(item.productId(), item.quantity()));

            BigDecimal price = new BigDecimal(product.price());
            BigDecimal lineTotal = price.multiply(BigDecimal.valueOf(item.quantity()));

            return OrderItem.builder()
                    .order(order)
                    .productId(item.productId())
                    .quantity(item.quantity())
                    .priceAtPurchase(price)
                    .build();
        }).toList();

        // calculate total
        total = items.stream()
                .map(i -> i.getPriceAtPurchase().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setItems(items);
        order.setTotalAmount(total);

        Order saved = orderRepository.save(order);

        return new OrderResponseDto(
                saved.getId(),
                saved.getUserId(),
                saved.getStatus(),
                saved.getTotalAmount(),
                saved.getItems().stream()
                        .map(i -> new OrderItemResponseDto(
                                i.getProductId(),
                                i.getQuantity(),
                                i.getPriceAtPurchase()))
                        .toList()
        );
    }

    @Override
    public List<OrderResponseDto> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId)
                .stream()
                .map(order -> new OrderResponseDto(
                        order.getId(),
                        order.getUserId(),
                        order.getStatus(),
                        order.getTotalAmount(),
                        order.getItems().stream()
                                .map(i -> new OrderItemResponseDto(
                                        i.getProductId(),
                                        i.getQuantity(),
                                        i.getPriceAtPurchase()))
                                .toList()
                ))
                .toList();
    }
}
