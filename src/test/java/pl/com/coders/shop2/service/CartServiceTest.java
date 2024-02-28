package pl.com.coders.shop2.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.com.coders.shop2.domain.Cart;
import pl.com.coders.shop2.domain.CartLineItem;
import pl.com.coders.shop2.domain.Product;
import pl.com.coders.shop2.domain.User;
import pl.com.coders.shop2.domain.dto.CartDto;
import pl.com.coders.shop2.exceptions.ProductNotFoundException;
import pl.com.coders.shop2.mapper.CartMapper;
import pl.com.coders.shop2.mapper.UserMapper;
import pl.com.coders.shop2.repository.CartRepository;
import pl.com.coders.shop2.repository.ProductRepository;
import pl.com.coders.shop2.repository.UserRepository;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartMapper cartMapper;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private CartService cartService;

    private User user;
    private Product product;
    private CartDto cartDto;
    private Cart userCart;
    private String productTitle = "SampleProduct";
    private int amount = 5;
    private String userEmail = "john@example.com";

    @BeforeEach
    void setUp() {
        userCart = new Cart();
        userCart.setId(1L);

        product = new Product();
        product.setId(1L);
        product.setName(productTitle);
        product.setQuantity(10);
        product.setPrice(BigDecimal.TEN);

        user = new User();
        user.setEmail(userEmail);

        cartDto = new CartDto();
        cartDto.setTotalPrice(BigDecimal.TEN);
        cartDto.setCartLineItems(Collections.emptyList());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userEmail, "pass1");
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
    }

    @Test
    void testAddProductToCart() throws ProductNotFoundException {
        // Mocking
        when(cartRepository.getCartForAuthUser(userEmail)).thenReturn(userCart);
        when(userRepository.findByEmail(anyString())).thenReturn(user);
        when(productRepository.getProductByName(anyString())).thenReturn(product);
        when(cartRepository.calculateCartTotalPrice(any())).thenReturn(BigDecimal.TEN);
        when(cartRepository.updateCart(any())).thenReturn(userCart);
        when(cartMapper.toDto(any())).thenReturn(cartDto);

        // Test
        CartDto result = cartService.addProductToCart(productTitle, amount);

        // Verify
        assertEquals(cartDto, result);
        verify(cartRepository, times(1)).getCartForAuthUser(userEmail);
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(productRepository, times(1)).getProductByName(productTitle);
        verify(cartRepository, times(1)).updateCart(userCart);
        verify(cartMapper, times(1)).toDto(userCart);
    }

    @Test
    void testGetOrCreateUserCart() {
        // Mocking
        String userEmail = "john@example.com";
        Cart userCart = new Cart();

        when(cartRepository.getCartForAuthUser(userEmail)).thenReturn(userCart);
        when(cartRepository.createCart(any(User.class))).thenReturn(userCart);

        // Test
        Cart result = cartService.getOrCreateUserCart(userEmail);

        // Verify
        assertNotNull(result);
        verify(cartRepository, times(1)).getCartForAuthUser(userEmail);
    }

    @Test
    void testDeleteByIndex() {
        // Mocking
        Long cartId = 1L;
        int cartIndex = 2;

        CartLineItem cartLineItem = new CartLineItem();
        cartLineItem.setCartIndex(cartIndex);

        when(cartRepository.getCartByCartId(cartId)).thenReturn(userCart);
        when(cartRepository.calculateCartTotalPrice(any())).thenReturn(BigDecimal.ZERO);
        when(cartRepository.updateCart(any())).thenReturn(userCart);

        // Test
        cartService.deleteByIndex(cartId, cartIndex);

        // Verify
        assertTrue(userCart.getCartLineItems().isEmpty());
        verify(cartRepository, times(1)).deleteCartAndItems(cartId);
        verify(cartRepository, times(1)).getCartByCartId(cartId);
    }

    @Test
    void testGetCartForAuthUser() {
        // Mocking
        String userEmail = "john@example.com";
        Cart cartForUser = new Cart();

        when(cartRepository.getCartForAuthUser(userEmail)).thenReturn(cartForUser);
        when(cartMapper.toDto(cartForUser)).thenReturn(new CartDto());

        // Test
        CartDto result = cartService.getCartForAuthUser(userEmail);

        // Verify
        assertNotNull(result);
        verify(cartRepository, times(1)).getCartForAuthUser(userEmail);
        verify(cartMapper, times(1)).toDto(cartForUser);
    }
}
