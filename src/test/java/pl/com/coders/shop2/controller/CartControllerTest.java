package pl.com.coders.shop2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.com.coders.shop2.domain.Cart;
import pl.com.coders.shop2.domain.dto.CartDto;
import pl.com.coders.shop2.exceptions.ProductNotFoundException;
import pl.com.coders.shop2.service.CartService;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc

class CartControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CartService cartService;

    private CartDto cartDto;
    private Cart cart;
    private String userEmail;
    private String productTitle;
    private int amount;

    @BeforeEach
    void setUp() {
        cartDto = new CartDto();
        cartDto.setUserName("Doe");
        amount = 20;
        productTitle = "Product 2";
        userEmail = "john@example.com";
    }

    @Test
    void addProductToCart() throws ProductNotFoundException, Exception {
        when(cartService.addProductToCart(productTitle, amount)).thenReturn(cartDto);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/carts/{productTitle}/{amount}/addProductToCart", productTitle, amount)
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + base64("john@example.com:pass1"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        String responseContent = result.getResponse().getContentAsString();
        CartDto responseCart = objectMapper.readValue(responseContent, CartDto.class);
        assertEquals(cartDto.getUserName(), responseCart.getUserName());
        verify(cartService, times(1)).addProductToCart(productTitle,amount);
    }

    @Test
    void deleteCartByIndex() throws Exception {
        // Given
        int cartIndex = 1;
        Long cartId = 302L;
        Mockito.when(cartService.deleteByIndex(cartId, cartIndex)).thenReturn(true);

        // When
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/carts/{cartIndex}/{cartId}", cartIndex, cartId)
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + base64("john@example.com:pass1"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Then
        verify(cartService, times(1)).deleteByIndex(cartId, cartIndex);
    }

    @Test
    void getCartsForAuthUser() throws Exception {
        // Given
        when(cartService.getCartForAuthUser(userEmail)).thenReturn(cartDto);

        // When
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/carts/getByEmail/{userEmail}", userEmail)
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + base64("john@example.com:pass1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Then
        String jsonResponse = result.getResponse().getContentAsString();
        if (!jsonResponse.isEmpty()) {
            CartDto responseCart = objectMapper.readValue(jsonResponse, CartDto.class);
        }
        assertNotNull(result.getResponse().getContentType());
    }

    private String base64(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes());
    }
}