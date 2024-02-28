package pl.com.coders.shop2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.coders.shop2.domain.dto.CategoryDto;
import pl.com.coders.shop2.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/byCategory/{categoryType}")
    public ResponseEntity<CategoryDto> getCategoryByName(@PathVariable String categoryType) {
        CategoryDto categoryByName = categoryService.getCategoryByName(categoryType);
        return ResponseEntity.status(HttpStatus.OK).body(categoryByName);
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = categoryService.getAllCategories();
        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }
}
