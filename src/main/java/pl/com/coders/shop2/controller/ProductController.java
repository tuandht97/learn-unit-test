package pl.com.coders.shop2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.com.coders.shop2.domain.CategoryType;
import pl.com.coders.shop2.domain.dto.ProductDto;
import pl.com.coders.shop2.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductDto> create(@RequestBody ProductDto productDto) {
        ProductDto createdProduct = productService.create(productDto);
        return ResponseEntity.status(HttpStatus.OK).body(createdProduct);
    }

    @GetMapping("/{name}")
    public ResponseEntity<ProductDto> getName(@PathVariable String name) {
        ProductDto productDto = productService.getByName(name);
        return ResponseEntity.status(HttpStatus.OK).body(productDto);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<ProductDto> getById(@PathVariable Long id) {
        ProductDto productDto = productService.getById(id);
        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(@RequestBody ProductDto productDto, @PathVariable Long id) {
        ProductDto updatedProduct = productService.update(productDto, id);
        return ResponseEntity.ok(updatedProduct);
    }

    @GetMapping("/getProduct")
    public ResponseEntity<List<ProductDto>> getAllProd() {
        List<ProductDto> productList = productService.getAllProd();
        return ResponseEntity.status(HttpStatus.OK).body(productList);
    }

    @GetMapping("/getByCategory/{categoryType}")
    public ResponseEntity<List<ProductDto>> getProductsByCategory(@PathVariable CategoryType categoryType) {
        List<ProductDto> productDto = productService.getProductsByCategory(categoryType);
        return ResponseEntity.ok(productDto);
    }
}
