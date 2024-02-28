package pl.com.coders.shop2.domain.dto;

import lombok.*;
import pl.com.coders.shop2.domain.OrderLineItem;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderLineItemDto {
    private UUID orderId;
    private long productId;
    private int quantity;
}
