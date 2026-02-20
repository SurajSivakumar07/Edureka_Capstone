package com.fytzi.orderservice.service.impl;

import com.fytzi.orderservice.client.InventoryClient;
import com.fytzi.orderservice.client.ProductClient;
import com.fytzi.orderservice.dto.*;
import com.fytzi.orderservice.entity.*;
import com.fytzi.orderservice.exception.InvalidProductIdException;
import com.fytzi.orderservice.repository.OrderRepository;
import com.fytzi.orderservice.service.OrderService;
import feign.FeignException;
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
    public OrderResponseDto createOrder(CreateOrderRequest request, String userId) {
        log.info("Creating order for user: {}", userId);

        validateProductsExist(request);
        placeOrderInProductService(request);

        Order order = mapToOrderEntity(request, userId);
        Order saved = orderRepository.save(order);

        return mapToOrderResponseDto(saved);
    }

    private void validateProductsExist(CreateOrderRequest request) {
        List<Long> productIds = request.items().stream()
                .map(CreateOrderItemRequest::productId)
                .filter(Objects::nonNull)
                .toList();
        log.info("Validating productIds: {}", productIds);

        ProductListRequest prodList = new ProductListRequest(productIds);

        try {
            Boolean exists = productClient.checkProductExsists(prodList).getBody();
            if (Boolean.FALSE.equals(exists)) {
                throw new InvalidProductIdException("One or more product IDs are invalid");
            }
        } catch (FeignException ex) {
            log.error("Error validating products: {}", ex.getMessage());
            throw new InvalidProductIdException(ex.contentUTF8());
        }
    }

    private void placeOrderInProductService(CreateOrderRequest request) {
        try {
            productClient.placeOrder(request);
        } catch (FeignException.BadRequest ex) {
            log.error("Bad request to product service: {}", ex.getMessage());
            throw new InvalidProductIdException("Invalid product id sent");
        } catch (FeignException ex) {
            log.error("Product service error: {}", ex.getMessage());
            throw new RuntimeException("Product service is currently unavailable");
        }
    }

    private Order mapToOrderEntity(CreateOrderRequest request, String userId) {
        Order order = Order.builder()
                .userId(Long.parseLong(userId))
                .status("CREATED")
                .createdAt(LocalDateTime.now())
                .totalAmount(BigDecimal.ZERO) // Total should be calculated or passed
                .build();

        List<OrderItem> items = request.items().stream().map(item -> {
            // TODO: Fetch actual price from Product service instead of hardcoding
            BigDecimal price = new BigDecimal(9000);

            return OrderItem.builder()
                    .order(order)
                    .productId(item.productId())
                    .quantity(item.quantity())
                    .priceAtPurchase(price)
                    .build();
        }).toList();

        order.setItems(items);
        return order;
    }

    private OrderResponseDto mapToOrderResponseDto(Order order) {
        return new OrderResponseDto(
                order.getId(),
                order.getUserId(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getItems().stream()
                        .map(i -> new OrderItemResponseDto(
                                i.getProductId(),
                                i.getQuantity(),
                                i.getPriceAtPurchase()))
                        .toList());
    }

    @Override
    public List<OrderResponseDto> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId)
                .stream()
                .map(this::mapToOrderResponseDto)
                .toList();
    }
}
