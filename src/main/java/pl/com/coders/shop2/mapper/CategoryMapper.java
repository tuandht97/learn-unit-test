package pl.com.coders.shop2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.com.coders.shop2.domain.Category;
import pl.com.coders.shop2.domain.dto.CategoryDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(source = "name", target = "title")
    CategoryDto categoryToDto(Category category);

    @Mapping(source = "name", target = "title")
    List<CategoryDto> categoriesToDtos(List<Category> categories);
}
