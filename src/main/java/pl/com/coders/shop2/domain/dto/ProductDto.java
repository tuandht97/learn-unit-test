package pl.com.coders.shop2.domain.dto;

import lombok.*;
import pl.com.coders.shop2.domain.CategoryType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString

public class ProductDto {

   // @JsonIgnore
    private CategoryType categoryType;
    private String name;
    private String description;
    private BigDecimal price;
    private int quantity;

    private LocalDateTime created;
    private LocalDateTime updated;
}
