package pl.com.coders.shop2.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.com.coders.shop2.domain.Category;

import java.util.List;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {
    boolean existsByName(String name);

    Category getCategoryById(Long categoryId);

    Category getCategoryByName(String name);

    List<Category> findAll();
}