package com.fytzi.orderservice.service.impl;

import com.fytzi.orderservice.client.InventoryClient;
import com.fytzi.orderservice.client.ProductClient;
import com.fytzi.orderservice.dto.CreateOrderItemRequest;
import com.fytzi.orderservice.dto.CreateOrderRequest;
import com.fytzi.orderservice.dto.OrderItemResponseDto;
import com.fytzi.orderservice.dto.OrderResponseDto;
import com.fytzi.orderservice.dto.ProductDto;
import com.fytzi.orderservice.dto.ProductListRequest;
import com.fytzi.orderservice.entity.Order;
import com.fytzi.orderservice.entity.OrderItem;
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
import java.util.Map;
import java.util.stream.Collectors;

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

        List<Long> productIds = request.getItems().stream()
                .map(CreateOrderItemRequest::getProductId)
                .toList();

        List<ProductDto> productDetails = productClient.getProductsDetails(new ProductListRequest(productIds))
                .getBody();

        placeOrderInProductService(request);

        Order order = mapToOrderEntity(request, userId, productDetails);
        Order saved = orderRepository.save(order);

        return mapToOrderResponseDto(saved);
    }

    private void validateProductsExist(CreateOrderRequest request) {
        List<Long> productIds = request.getItems().stream()
                .map(CreateOrderItemRequest::getProductId)
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
            throw new InvalidProductIdException("No Enough of stock" + ex.getMessage());
        } catch (FeignException ex) {
            log.error("Product service error: {}", ex.getMessage());
            throw new RuntimeException("Product service is currently unavailable");
        }
    }

    private Order mapToOrderEntity(CreateOrderRequest request, String userId, List<ProductDto> productDetails) {
        Order order = Order.builder()
                .userId(Long.parseLong(userId))
                .status("CREATED")
                .createdAt(LocalDateTime.now())
                .build();

        Map<Long, java.math.BigDecimal> priceMap = productDetails.stream()
                .collect(Collectors.toMap(ProductDto::getId, ProductDto::getPrice));

        List<OrderItem> items = request.getItems().stream().map(item -> {
            BigDecimal price = priceMap.getOrDefault(item.getProductId(), BigDecimal.ZERO);

            return OrderItem.builder()
                    .order(order)
                    .productId(item.getProductId())
                    .quantity(item.getQuantity())
                    .priceAtPurchase(price)
                    .build();
        }).toList();

        BigDecimal totalAmount = items.stream()
                .map(item -> item.getPriceAtPurchase().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setItems(items);
        order.setTotalAmount(totalAmount);
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

    @Override
    public List<OrderResponseDto> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToOrderResponseDto)
                .toList();
    }
}
