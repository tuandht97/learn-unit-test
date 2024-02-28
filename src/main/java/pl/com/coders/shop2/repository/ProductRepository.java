package pl.com.coders.shop2.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.com.coders.shop2.domain.Category;
import pl.com.coders.shop2.domain.CategoryType;
import pl.com.coders.shop2.domain.Product;
import pl.com.coders.shop2.exceptions.ProductWithGivenIdNotExistsException;
import pl.com.coders.shop2.exceptions.ProductWithGivenTitleExistsException;
import pl.com.coders.shop2.exceptions.ProductWithGivenTitleNotExistsException;

import javax.persistence.*;
import java.util.List;
@Repository
public class ProductRepository {
    @PersistenceContext
    private final EntityManager entityManager;
    private final CategoryRepository categoryRepository;

    public ProductRepository(EntityManager entityManager, CategoryRepository categoryRepository) {
        this.entityManager = entityManager;
        this.categoryRepository = categoryRepository;
    }

    @Transactional()
    public Product add(Product product) throws ProductWithGivenTitleExistsException {
        if (product.getName() == null) {
            throw new ProductWithGivenTitleNotExistsException("Product with the given title doesn't exist");
        }
        return entityManager.merge(product);
    }

    public Product getProductById(Long id) throws ProductWithGivenIdNotExistsException {
        Product product = entityManager.find(Product.class, id);
        if (product == null) {
            throw new ProductWithGivenIdNotExistsException("Product with the given Id " + id + " doesn't exist");
        }
        return product;
    }

    @Transactional
    public List<Product> getProductsByCategory(CategoryType categoryType) {
        Category category = categoryRepository.getCategoryByName(String.valueOf(categoryType));
        String jpql = "SELECT p FROM Product p WHERE p.category = :category";
        return entityManager.createQuery(jpql, Product.class)
                .setParameter("category", category)
                .getResultList();
    }

    @Transactional
    public boolean delete(Long id) {
        Product product = entityManager.find(Product.class, id);

        if (product != null) {
            entityManager.createQuery("DELETE FROM CartLineItem cli WHERE cli.product = :product")
                    .setParameter("product", product)
                    .executeUpdate();

            entityManager.createQuery("DELETE FROM OrderLineItem oli WHERE oli.product = :product")
                    .setParameter("product", product)
                    .executeUpdate();

            entityManager.remove(product);
            return true;
        }

        return false;
    }

    @Transactional
    public Product update(Product product, Long id) throws ProductWithGivenIdNotExistsException {
        Query updatedEntities = entityManager.createQuery(
                        "UPDATE Product p SET p.name = :name, p.description = :description, p.price = :price, p.quantity = :quantity WHERE p.id = :id")
                .setParameter("name", product.getName())
                .setParameter("description", product.getDescription())
                .setParameter("price", product.getPrice())
                .setParameter("quantity", product.getQuantity())
                .setParameter("id", id);

        if (updatedEntities == null) {
            throw new ProductWithGivenIdNotExistsException("Product with the given ID does not exist.");
        }
        return entityManager.find(Product.class, id);
    }

    @Transactional
    public List<Product> findAllProd() {
        return entityManager.createQuery("SELECT p FROM Product p", Product.class)
                .getResultList();
    }

    @Transactional
    public void deleteAll() {
        entityManager.createQuery("DELETE FROM Product").executeUpdate();
    }

    @Transactional
    public Product getProductByName(String name) {
        String jpql = "SELECT p FROM Product p WHERE p.name = :name";
        TypedQuery<Product> query = entityManager.createQuery(jpql, Product.class);
        query.setParameter("name", name);
        return query.getResultList().stream().findFirst().orElseThrow(() -> new EntityNotFoundException("Product not found"));

    }
}