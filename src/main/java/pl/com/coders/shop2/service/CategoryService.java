package pl.com.coders.shop2.service;

import org.springframework.stereotype.Service;
import pl.com.coders.shop2.domain.Category;
import pl.com.coders.shop2.domain.dto.CategoryDto;
import pl.com.coders.shop2.mapper.CategoryMapper;
import pl.com.coders.shop2.repository.CategoryRepository;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public CategoryDto getCategoryByName(String categoryType) {
        Category categoryByName = categoryRepository.getCategoryByName(categoryType);
        return categoryMapper.categoryToDto(categoryByName);
    }

    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categoryMapper.categoriesToDtos(categories);
    }
}
