package pl.com.coders.shop2.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.com.coders.shop2.domain.Cart;
import pl.com.coders.shop2.domain.Order;
import pl.com.coders.shop2.domain.OrderLineItem;
import pl.com.coders.shop2.domain.Product;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;

    private List<Order> allOrders;
    private Cart cart;
    private Order order;



    @BeforeEach
    void setUp() {
        cart = cartRepository.getCartByCartId(1L);
    }

    @Test
    void createOrderFromCart() {
        Order orderByUser = orderRepository.createOrderFromCart(cart);
        assertNotNull(orderByUser);
    }

    @Test
    void createOrderLineItem() {
        Order orderByUser = orderRepository.createOrderFromCart(cart);
        Product product = productRepository.getProductById(1L);
        int quantity = 20;
        OrderLineItem orderLineItem = orderRepository.createOrderLineItem(orderByUser, product, quantity);
        assertNotNull(orderLineItem);
    }

    @Test
    void delete() {
        boolean deletedOrders = orderRepository.delete(UUID.fromString("a0a1ab07-d158-4e89-8b42-2fd8c677147f"));
        assertTrue(deletedOrders);
    }

    @Test
    void findAllOrders() {
        List<Order> orders = orderRepository.findAllOrders();
        assertEquals(2, orders.size());
    }
}