package pl.com.coders.shop2.domain.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CartLineItemDto {
    private String productTitle;
    private int cartLineQuantity;
    private BigDecimal cartLinePrice;
    private int cartIndex;
}
