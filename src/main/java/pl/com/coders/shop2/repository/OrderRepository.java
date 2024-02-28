package pl.com.coders.shop2.repository;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.com.coders.shop2.domain.*;
import pl.com.coders.shop2.exceptions.UserNotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Repository
public class OrderRepository {

    @PersistenceContext
    private final EntityManager entityManager;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    public OrderRepository(EntityManager entityManager, UserRepository userRepository, CartRepository cartRepository) {
        this.entityManager = entityManager;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }

    @Transactional
    public Order createOrderFromCart(Cart cart) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setStatus("nowy");
        order.setTotalAmount(cart.getTotalPrice());
        order.setOrderLineItems(new ArrayList<>());
        order.setCreated(LocalDateTime.now());
        order.setCreated(LocalDateTime.now());
        entityManager.persist(order);
        return order;
    }

    @Transactional
    public OrderLineItem createOrderLineItem(Order order, Product product, int quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrder(order);
        orderLineItem.setProduct(product);
        orderLineItem.setQuantity(quantity);
        orderLineItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        order.addOrderLineItems(orderLineItem);
        entityManager.persist(orderLineItem);
        return orderLineItem;
    }

    @Transactional
    public boolean delete(UUID id) {
        Order order = entityManager.find(Order.class, id);
        if (order != null) {
            entityManager.remove(order);
            return true;
        }
        throw new EntityNotFoundException("Order with ID " + id + " not found");
    }

    @Transactional
    public List<Order> findAllOrders() {
        TypedQuery<Order> query = entityManager.createQuery("SELECT o FROM Order o", Order.class);
        return query.getResultList();
    }

    public Order getOrdersByUser(String userEmail) {
        String authenticatedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(authenticatedUser);
        if (user == null) {
            throw new UserNotFoundException("User not found for email: " + authenticatedUser);
        }
        Cart cart = cartRepository.getCartForAuthUser(authenticatedUser);

        Order existingOrder = entityManager.createQuery("SELECT o FROM Order o JOIN FETCH o.user WHERE o.user = :user", Order.class)
                .setParameter("user", user)
                .getSingleResult();

        if (existingOrder == null) {
            existingOrder = createOrderFromCart(cart);
        }
        return existingOrder;
    }
}


