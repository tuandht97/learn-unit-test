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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.com.coders.shop2.domain.Category;
import pl.com.coders.shop2.domain.CategoryType;
import pl.com.coders.shop2.domain.Product;
import pl.com.coders.shop2.domain.dto.CartDto;
import pl.com.coders.shop2.domain.dto.ProductDto;
import pl.com.coders.shop2.service.ProductService;

import java.math.BigDecimal;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    private CategoryType categoryType;
    private ProductDto productDto;
    private Product product;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        categoryType = CategoryType.ELEKTRONIKA;
        Category category = createSampleCategory();
        productDto = createSampleDtoProduct(categoryType);
        when(productService.create(productDto)).thenReturn(productDto);
    }

    @Test
    void create() throws Exception {
        when(productService.create(any())).thenReturn(productDto);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isOk())
                .andReturn();
        String responseContent = result.getResponse().getContentAsString();
        ProductDto responseProduct = objectMapper.readValue(responseContent, ProductDto.class);
        assertEquals(productDto.getName(), responseProduct.getName());
        verify(productService, times(1)).create(any());
    }


    @Test
    void get() throws Exception {
        Long productId = 1L;
        // Given
        when(productService.getById(productId)).thenReturn(productDto);

        // When
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/product/getById/{productId}", productId)
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + base64("john@example.com:pass1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Then
        String jsonResponse = result.getResponse().getContentAsString();
        if (!jsonResponse.isEmpty()) {
            ProductDto responseProduct = objectMapper.readValue(jsonResponse, ProductDto.class);
        }
        assertNotNull(result.getResponse().getContentType());
    }


    @Test
    void delete() throws Exception {
        long productId = 1L;
        when(productService.delete(productId)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/product/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).delete(productId);
    }

    @Test
    void update() throws Exception {
            Long productId = 1L;
            ProductDto updatedProduct = createSampleDtoProduct(categoryType);
            String json = objectMapper.writeValueAsString(updatedProduct);

            when(productService.update(any(), eq(productId))).thenReturn(updatedProduct);

            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/product/{id}", productId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andReturn();

            String responseContent = result.getResponse().getContentAsString();
            ProductDto responseProduct = objectMapper.readValue(responseContent, ProductDto.class);

        assertEquals(productDto.getName(), responseProduct.getName());
        verify(productService, times(1)).update(any(), any());
         }

    private ProductDto createSampleDtoProduct(CategoryType categoryType) {
        return ProductDto.builder()
                .name("Sample Product")
                .description("Sample Description")
                .price(BigDecimal.valueOf(19.99))
                .quantity(10)
                .categoryType(categoryType)
                .build();
    }

    private Product createSampleProduct(Category category) {
        return Product.builder()
                .category(category)
                .name("Sample Product")
                .description("Sample Description")
                .price(BigDecimal.valueOf(19.99))
                .quantity(10)
                .build();
    }

    private Category createSampleCategory() {
        return Category.builder()
                .name("Sample Category")
                .build();
    }
    private String base64(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes());
    }
}
