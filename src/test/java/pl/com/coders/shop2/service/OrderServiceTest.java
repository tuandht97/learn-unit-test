package pl.com.coders.shop2.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.com.coders.shop2.domain.Order;
import pl.com.coders.shop2.domain.OrderLineItem;
import pl.com.coders.shop2.domain.Product;
import pl.com.coders.shop2.domain.User;
import pl.com.coders.shop2.domain.dto.CartDto;
import pl.com.coders.shop2.domain.dto.CartLineItemDto;
import pl.com.coders.shop2.domain.dto.OrderDto;
import pl.com.coders.shop2.mapper.CartMapper;
import pl.com.coders.shop2.mapper.OrderMapper;
import pl.com.coders.shop2.repository.CartRepository;
import pl.com.coders.shop2.repository.OrderRepository;
import pl.com.coders.shop2.repository.ProductRepository;
import pl.com.coders.shop2.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartService cartService;

    @Mock
    private CartMapper cartMapper;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

    private Order order;
    private CartDto cartDto;
    private String userEmail;
    private User user;
    private List<OrderLineItem> orderLineItems;

    @BeforeEach
    void setUp() {
        userEmail = "john@example.com";
        order = new Order();
        order.setId(UUID.randomUUID());

        CartLineItemDto cartLineItemDto = new CartLineItemDto();
        cartDto = new CartDto();
        cartDto.setCartLineItems(Collections.singletonList(cartLineItemDto));

        user = new User();
        user.setEmail(userEmail);

        when(userRepository.findByEmail(anyString())).thenReturn(user);

        orderLineItems = new ArrayList<>();
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItems.add(orderLineItem);
        when(orderRepository.createOrderLineItem(any(), any(), anyInt())).thenReturn(orderLineItem);
    }

    @Test
    void testCreateOrderFromCart() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(userEmail, "pass1");
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        when(cartService.getCartForAuthUser(anyString())).thenReturn(cartDto);
        when(orderRepository.createOrderFromCart(any())).thenReturn(order);
        when(orderMapper.orderToDto(any())).thenReturn(new OrderDto());

        OrderDto orderFromCart = orderService.createOrderFromCart(userEmail);
        assertNotNull(orderFromCart);

        verify(userRepository, times(1)).findByEmail(anyString());

        verify(cartService, times(1)).getCartForAuthUser(userEmail);
        verify(orderRepository, times(1)).createOrderFromCart(any());
        verify(orderMapper, times(1)).orderToDto(order);
    }

    @Test
    void testCreateOrderLineItems() {
        Product mockProduct = new Product();
        when(productRepository.getProductByName(anyString())).thenReturn(mockProduct);

        OrderLineItem orderLineItem = new OrderLineItem();
        when(orderRepository.createOrderLineItem(any(), any(), anyInt())).thenReturn(orderLineItem);

        List<OrderLineItem> result = orderService.createOrderLineItems(cartDto, order, orderLineItems);
        assertNotNull(result);
    }

    @Test
    void testDelete() {
        UUID orderId = order.getId();
        when(orderRepository.delete(orderId)).thenReturn(true);
        assertTrue(orderService.delete(orderId));
    }


    @Test
    void testGetOrdersByUser() {
        when(orderRepository.getOrdersByUser(anyString())).thenReturn(new Order());
        assertDoesNotThrow(() -> orderService.getOrdersByUser(userEmail));
    }
}
