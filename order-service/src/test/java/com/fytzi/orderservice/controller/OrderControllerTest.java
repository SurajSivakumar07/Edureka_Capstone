package com.fytzi.orderservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fytzi.orderservice.dto.CreateOrderRequest;
import com.fytzi.orderservice.dto.OrderResponseDto;
import com.fytzi.orderservice.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateOrder() throws Exception {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setItems(new ArrayList<>());

        OrderResponseDto responseDto = OrderResponseDto.builder()
                .orderId(1L)
                .userId(1L)
                .status("CREATED")
                .totalAmount(new BigDecimal("100.00"))
                .build();

        Mockito.when(orderService.createOrder(any(CreateOrderRequest.class), eq("1")))
                .thenReturn(responseDto);

        mockMvc.perform(post("/orders/create")
                .header("X-USER-ID", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId").value(1L))
                .andExpect(jsonPath("$.status").value("CREATED"));
    }
}
