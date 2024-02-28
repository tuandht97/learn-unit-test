package pl.com.coders.shop2.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
