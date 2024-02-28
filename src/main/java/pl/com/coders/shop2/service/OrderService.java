package pl.com.coders.shop2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.coders.shop2.domain.Order;
import pl.com.coders.shop2.domain.OrderLineItem;
import pl.com.coders.shop2.domain.User;
import pl.com.coders.shop2.domain.dto.CartDto;
import pl.com.coders.shop2.domain.dto.CartLineItemDto;
import pl.com.coders.shop2.domain.dto.OrderDto;
import pl.com.coders.shop2.exceptions.EmptyCartException;
import pl.com.coders.shop2.exceptions.UserNotFoundException;
import pl.com.coders.shop2.mapper.CartMapper;
import pl.com.coders.shop2.mapper.OrderMapper;
import pl.com.coders.shop2.repository.CartRepository;
import pl.com.coders.shop2.repository.OrderRepository;
import pl.com.coders.shop2.repository.ProductRepository;
import pl.com.coders.shop2.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final CartRepository cartRepository;
    private final CartService cartService;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;

    @Transactional
    public OrderDto createOrderFromCart(String userEmail) {
        String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(loggedUser);
        if (!userEmail.equals(loggedUser)) {
            throw new UserNotFoundException("Podany email nie jest zalogowanym użytkownikiem.");
        }
        CartDto cartForAuthUser = cartService.getCartForAuthUser(loggedUser);
        Order order = orderRepository.createOrderFromCart(cartMapper.toCart(cartForAuthUser));
        order.setUser(user);
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        createOrderLineItems(cartForAuthUser, order, orderLineItems);
        cartRepository.deleteCartAndItems(cartForAuthUser.getId());
        return orderMapper.orderToDto(order);
    }

    public List<OrderLineItem> createOrderLineItems(CartDto cartForAuthUser, Order order, List<OrderLineItem> orderLineItems) {
        for (CartLineItemDto dto : cartForAuthUser.getCartLineItems()) {
            OrderLineItem orderLineItem = orderRepository.createOrderLineItem(order,
                    productRepository.getProductByName(dto.getProductTitle()), dto.getCartLineQuantity());
            orderLineItems.add(orderLineItem);
        }
        return orderLineItems;
    }

    public boolean delete(UUID orderId) {
        return orderRepository.delete(orderId);
    }

    public OrderDto getOrdersByUser(String userEmail) throws EmptyCartException {
        Order userOrder = orderRepository.getOrdersByUser(userEmail);
        return orderMapper.orderToDto(userOrder);
    }
}

