package pl.com.coders.shop2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import pl.com.coders.shop2.domain.*;
import pl.com.coders.shop2.domain.dto.OrderDto;
import pl.com.coders.shop2.domain.dto.OrderLineItemDto;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(source = "user", target = "userLastName", qualifiedByName = "mapUserToLastName")
    @Mapping(source = "orderLineItems", target = "orderLineDtoItems", qualifiedByName = "mapOrderLineItems")
    OrderDto orderToDto(Order order);

    @Mapping(source = "userLastName", target = "user", qualifiedByName = "mapLastNameToUser")
    Order dtoToOrder(OrderDto orderDto);

    @Named("mapUserToLastName")
    default String mapUserToLastName(User user) {
        return user != null ? user.getLastName() +user.getFirstName() : null;
    }

    @Named("mapLastNameToUser")
    default User mapLastNameToUser(String userLastName) {
        User user = new User();
        user.setLastName(userLastName);
        return user;
    }

    @Named("mapOrderLineItems")
    default List<OrderLineItemDto> mapOrderLineItems(Set<OrderLineItem> orderLineItems) {
        System.out.println("Mapping OrderLineItems...");
        if (orderLineItems != null) {
            return orderLineItems.stream()
                    .map(this::mapOrderLineItem)
                    .collect(Collectors.toList());
        } else {
            System.out.println("OrderLineItems is null");
            return Collections.emptyList();
        }
    }
    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "product.id", target = "productId")
    OrderLineItemDto mapOrderLineItem(OrderLineItem orderLineItem);

}
