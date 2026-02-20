package com.fytzi.orderservice.service.impl;

import com.fytzi.orderservice.client.InventoryClient;
import com.fytzi.orderservice.client.ProductClient;
import com.fytzi.orderservice.dto.*;
import com.fytzi.orderservice.entity.*;
import com.fytzi.orderservice.repository.OrderRepository;
import com.fytzi.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final InventoryClient inventoryClient;

    @Override
    public OrderResponseDto createOrder(CreateOrderRequest request,String userId) {

        BigDecimal total = BigDecimal.ZERO;

        List<Long> productIds = request.items().stream()
                .map(CreateOrderItemRequest::productId)
                .filter(Objects::nonNull)
                .toList();
        log.info("productIds: {}", productIds);

        ProductListRequest prodList = new ProductListRequest(productIds);

        //calling produdct service to check if product id is valid
        Boolean result=productClient.checkProductExsists(prodList);
        log.info("value",request);

        Boolean checkIfQuantityReduced=false;
        //Check is quanity is present and place order
        if(result){
            checkIfQuantityReduced=productClient.placeOrder(request);
        }


        //userDetails in the list
        Order order = Order.builder()
                .userId(Long.parseLong(userId))
                .status("CREATED")
                .createdAt(LocalDateTime.now())
                .build();

        List<OrderItem> items = request.items().stream().map(item -> {

            BigDecimal price = new BigDecimal(9000);

            return OrderItem.builder()
                    .order(order)
                    .productId(item.productId())
                    .quantity(item.quantity())
                    .priceAtPurchase(price)
                    .build();
        }).toList();

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
