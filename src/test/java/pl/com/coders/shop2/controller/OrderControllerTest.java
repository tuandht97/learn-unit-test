package pl.com.coders.shop2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.com.coders.shop2.domain.dto.OrderDto;
import pl.com.coders.shop2.exceptions.EmptyCartException;
import pl.com.coders.shop2.service.OrderService;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private OrderService orderService;

    private OrderDto orderDto;
    private String userEmail;

    @BeforeEach
    void setUp() {
        orderDto = new OrderDto();
        orderDto.setUserLastName("Doe");
        orderDto.setTotalAmount(BigDecimal.valueOf(100.0));
        userEmail = "john@example.com";

        Authentication authentication = new UsernamePasswordAuthenticationToken(userEmail, "pass1");
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
    }

    @Test
    void saveOrder() throws Exception {
        when(orderService.createOrderFromCart(userEmail)).thenReturn(orderDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/orders/saveOrder/{userEmail}", userEmail)
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + base64("john@example.com:pass1"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        verify(orderService, times(1)).createOrderFromCart(any());
    }

    @Test
    void delete() throws Exception {
        // Given
        UUID orderId = UUID.fromString("8ee0c4f6-1b02-4a80-bf8b-ba70f5d9f49d");
        when(orderService.delete(orderId)).thenReturn(true);

        // When
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/orders/delete/{orderId}", orderId)
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + base64("john@example.com:pass1"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Then
        verify(orderService, times(1)).delete(orderId);
    }

    @Test
    void getOrdersByUser() throws EmptyCartException, Exception {
        // Given
        when(orderService.getOrdersByUser(userEmail)).thenReturn(orderDto);

        // When
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/orders/byUser/{userEmail}", userEmail)
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + base64("john@example.com:pass1")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Then
        String jsonResponse = result.getResponse().getContentAsString();
        if (!jsonResponse.isEmpty()) {
            OrderDto responseOrder = objectMapper.readValue(jsonResponse, OrderDto.class);
            assertEquals(orderDto.getUserLastName(), responseOrder.getUserLastName());
        }
        assertNotNull(result.getResponse().getContentType());
    }

    private String base64(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes());
    }
}