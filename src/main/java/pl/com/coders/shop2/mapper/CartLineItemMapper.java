package pl.com.coders.shop2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import pl.com.coders.shop2.domain.CartLineItem;
import pl.com.coders.shop2.domain.dto.CartLineItemDto;

@Mapper(componentModel = "spring", uses = ProductMapper.class)
public interface CartLineItemMapper {

        CartLineItemMapper INSTANCE = Mappers.getMapper(CartLineItemMapper.class);

    @Mapping(source = "cartLineItem.product.name", target = "productTitle")
    @Mapping(source = "cartIndex", target = "cartIndex")
    CartLineItemDto cartLineItemToDto(CartLineItem cartLineItem);

    @Mapping(source = "cartIndex", target = "cartIndex")
    CartLineItem dtoToCartLineItem(CartLineItemDto cartLineItemDto);
    }


