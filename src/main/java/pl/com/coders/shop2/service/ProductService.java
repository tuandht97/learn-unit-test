package pl.com.coders.shop2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.coders.shop2.domain.Category;
import pl.com.coders.shop2.domain.CategoryType;
import pl.com.coders.shop2.domain.Product;
import pl.com.coders.shop2.domain.dto.ProductDto;
import pl.com.coders.shop2.mapper.ProductMapper;
import pl.com.coders.shop2.repository.CategoryRepository;
import pl.com.coders.shop2.repository.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    public ProductDto create(ProductDto productDto) {
        Long categoryId = productDto.getCategoryType().getId();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + categoryId));

        Product product = productMapper.dtoToProduct(productDto);
        product.setCategory(category);

        Product savedProduct = productRepository.add(product);
        return productMapper.productToDto(savedProduct);
    }

    public ProductDto getById(Long id) {
        return productMapper.productToDto(productRepository.getProductById(id));
    }

    public ProductDto getByName(String name) {
        return productMapper.productToDto(productRepository.getProductByName(name));
    }

    public boolean delete(Long id) {
        productRepository.delete(id);
        return true;
    }

    public ProductDto update(ProductDto productDto, Long id) {
        Product product = productMapper.dtoToProduct(productDto);
        Product update = productRepository.update(product, id);
        return productMapper.productToDto(update);
    }

    public List<ProductDto> getAllProd() {
        List<Product> products = productRepository.findAllProd();
        return  productMapper.productsToDtos(products);
    }

    public List<ProductDto> getProductsByCategory(CategoryType categoryType) {
        List<Product> productsByCategory = productRepository.getProductsByCategory(categoryType);
        return productMapper.productsToDtos(productsByCategory);
    }
}