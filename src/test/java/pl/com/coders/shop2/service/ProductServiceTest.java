package pl.com.coders.shop2.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import pl.com.coders.shop2.domain.*;
import pl.com.coders.shop2.domain.dto.ProductDto;
import pl.com.coders.shop2.mapper.ProductMapper;
import pl.com.coders.shop2.repository.CategoryRepository;
import pl.com.coders.shop2.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductMapper productMapper;
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private ProductService productService;

    private ProductDto productDto;
    private Category category;
    private Product product;
    private CategoryType categoryType;
    private List<Product> allProducts;
    private List<ProductDto> allDtoProducts;

    @BeforeEach
    void setUp() {
        productDto = createSampleDtoProduct(CategoryType.MOTORYZACJA);
        category = createSampleCategory();
        product = createSampleProduct(category);
        allProducts = List.of(product);
        allDtoProducts = productMapper.productsToDtos(allProducts);
    }

    @Test
    void create() {
        when(categoryRepository.findById((2L))).thenReturn(Optional.of(category));
        when(productMapper.dtoToProduct(productDto)).thenReturn(product);
        when(productRepository.add(product)).thenReturn(product);
        when(productMapper.productToDto(product)).thenReturn(productDto);
        ProductDto createdProduct = productService.create(productDto);
        assertNotNull(createdProduct);
        assertEquals(productDto.getName(), createdProduct.getName());
        verify(categoryRepository, times(1)).findById((2L));
        verify(productMapper, times(1)).dtoToProduct(any());
        verify(productRepository, times(1)).add(any());
        verify(productMapper, times(1)).productToDto(any());
    }


    @Test
    void getById() {
        Long id = 1L;
        when(productRepository.getProductById(any())).thenReturn(product);
        when(productMapper.productToDto(any())).thenReturn(productDto);
        ProductDto resultProduct = productService.getById(id);
        assertNotNull(resultProduct);
        assertSame(productDto.getName(), resultProduct.getName());
        verify(productRepository, times(1)).getProductById(id);
        verify(productMapper, times(1)).productToDto(product);
    }

    @Test
    void getByName() {
        String name = "Sample product";
        when(productRepository.getProductByName(any())).thenReturn(product);
        when(productMapper.productToDto(any())).thenReturn(productDto);
        ProductDto resultProduct = productService.getByName(name);
        assertNotNull(resultProduct);
        assertSame(productDto.getName(), resultProduct.getName());
        verify(productRepository, times(1)).getProductByName(name);
        verify(productMapper, times(1)).productToDto(product);
    }

    @Test
    void getByCategory() {
        CategoryType category = categoryType.EDUKACJA;
        when(productRepository.getProductsByCategory(category)).thenReturn(allProducts);
        List<ProductDto> resultProducts = productService.getProductsByCategory(category);
        assertNotNull(resultProducts);
        verify(productRepository, times(1)).getProductsByCategory(category);
    }

    @Test
    void delete() {
        Long id = 1L;
        when(productRepository.delete(id)).thenReturn(true);
        boolean resultProduct = productService.delete(id);
        assertTrue(resultProduct);
        verify(productRepository, times(1)).delete(id);
    }

    @Test
    void update() {
        Long productId = 1L;
        when(productMapper.dtoToProduct(any())).thenReturn(product);
        when(productRepository.update(any(), any())).thenReturn(product);
        when(productMapper.productToDto(any())).thenReturn(productDto);
        ProductDto updatedProduct = productService.update(productDto, productId);
        assertNotNull(updatedProduct);
        assertSame(productDto, updatedProduct);
        verify(productMapper, times(1)).dtoToProduct(productDto);
        verify(productRepository, times(1)).update(product, productId);
        verify(productMapper, times(1)).productToDto(product);
    }

    private ProductDto createSampleDtoProduct(CategoryType categoryType) {
        return ProductDto.builder()
                .categoryType(categoryType)
                .name("Sample Product")
                .description("Sample Description")
                .price(BigDecimal.valueOf(19.99))
                .quantity(10)
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
                .name("Books")
                .build();
    }
}
