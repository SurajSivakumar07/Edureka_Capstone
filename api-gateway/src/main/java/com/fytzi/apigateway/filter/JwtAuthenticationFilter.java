package com.fytzi.apigateway.filter;


import com.fytzi.apigateway.exception.UserNotAuthorized;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        String path = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethod().name();

        log.info("hello");
        // 🚫 Public routes
        if (exchange.getRequest().getURI().getPath().contains("/auth") || exchange.getRequest().getURI().getPath().contains("/users")) {
            return chain.filter(exchange);
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        if (!jwtUtil.isTokenValid(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // ✅ Extract values from JWT
       String userId = jwtUtil.extractUserId(token);
        String role = jwtUtil.extractRole(token);


        boolean isInventoryWrite =
                path.startsWith("/inventory")
                        && ("POST".equals(method) || "PUT".equals(method) || "DELETE".equals(method));


        boolean isProductWrite =path.startsWith("/product")
                &&  ("POST".equals(method) || "PUT".equals(method) || "DELETE".equals(method));

        if(isProductWrite && !"ADMIN".equalsIgnoreCase(role)) {
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
             throw new UserNotAuthorized("User is not authorized");
        }

        if (isInventoryWrite && !"ADMIN".equalsIgnoreCase(role)) {
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            throw new UserNotAuthorized("User is not authorized");
        }


        // ➕ Add to headers for downstream services
        ServerWebExchange modifiedExchange = exchange.mutate()
                .request(r -> r
                        .header("X-USER-ID", userId)
                        .header("X-USER-ROLE", role)
                )
                .build();

        return chain.filter(modifiedExchange);
    }


    @Override
    public int getOrder() {
        return -1; // Run before other filters
    }
}

