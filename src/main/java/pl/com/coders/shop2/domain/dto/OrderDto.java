package pl.com.coders.shop2.domain.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.*;

@Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public class OrderDto {
    private String userLastName;
    private BigDecimal totalAmount;
    private List<OrderLineItemDto> orderLineDtoItems;

}

