package pl.com.coders.shop2.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.com.coders.shop2.domain.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CartRepositoryTest {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    private User user;
    private Cart createdCart;
    private Product product;


    @BeforeEach
    void setUp() {
        user = new User("john@example.com", "John", "Doe", "pass1");
        Category category = categoryRepository.getCategoryById(1L);
        product = new Product(category, "Product 6", "Product 6 Description", BigDecimal.valueOf(300), 3000);
        userRepository.create(user);
        createdCart = cartRepository.createCart(user);
        String authenticatedUsername = "john@example.com";
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authenticatedUsername, null));
    }

    @Test
    void createCart() {
        assertNotNull(createdCart);
        assertEquals(createdCart.getUser().getEmail(), user.getEmail());
    }

    @Test
    void updateCart() {
        Cart newCart = new Cart(1, BigDecimal.TEN, user, LocalDateTime.now(), LocalDateTime.now());
        Cart updatedCart = cartRepository.updateCart(createdCart);
        assertEquals(updatedCart.getUser().getEmail(), user.getEmail());
    }

    @Test
    void createCartLineItem() {
        CartLineItem createdCartLineItem = cartRepository.createCartLineItem(1000, createdCart, product);
        assertNotNull(createdCartLineItem);
        assertEquals(createdCart, createdCartLineItem.getCart());
    }

    @Test
    void updateCartLineItem() {
        CartLineItem createdCartLineItem = cartRepository.createCartLineItem(1000, createdCart, product);
        CartLineItem newCartLineItem = new CartLineItem(1L, createdCart, product, 20, BigDecimal.TEN, 1);
        CartLineItem updatedCartLineItem = cartRepository.updateCartLineItem(10, createdCartLineItem, product);
        assertNotNull(updatedCartLineItem);
        assertEquals(updatedCartLineItem.getCartIndex(), newCartLineItem.getCartIndex());
    }

    @Test
    void getCartForAuthUser() {
        String authenticatedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        Cart cartForUser = cartRepository.getCartForAuthUser(authenticatedUser);
        assertNotNull(cartForUser);
        assertTrue(!cartForUser.getCartLineItems().isEmpty(), "Cart should not be empty");

    }

    @Test
    void deleteCartAndItems() {
        boolean deletedCart = cartRepository.deleteCartAndItems(2L);
        assertTrue(deletedCart);
    }

    @Test
    void getCartByCartId() {
        Cart cartById = cartRepository.getCartByCartId(1L);
        assertNotNull(cartById);
    }
}