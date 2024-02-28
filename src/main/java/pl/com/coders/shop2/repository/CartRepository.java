package pl.com.coders.shop2.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.com.coders.shop2.domain.Cart;
import pl.com.coders.shop2.domain.CartLineItem;
import pl.com.coders.shop2.domain.Product;
import pl.com.coders.shop2.domain.User;
import pl.com.coders.shop2.exceptions.UserNotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;

@Transactional
@Repository
@RequiredArgsConstructor
public class CartRepository {

    @PersistenceContext
    private final EntityManager entityManager;
    private final UserRepository userRepository;

    public CartLineItem createCartLineItem(int amount, Cart cart, Product product) {
        CartLineItem cartLineItem = new CartLineItem();
        cartLineItem.setCart(cart);
        cartLineItem.setProduct(product);
        cartLineItem.setCartLineQuantity(amount);
        cartLineItem.setCartLinePrice(product.getPrice().multiply(BigDecimal.valueOf(amount)));
        cartLineItem.setCartIndex(generateCartIndex(cart));
        cart.addCartLineItem(cartLineItem);
        entityManager.persist(cartLineItem);
        return cartLineItem;
    }

    public CartLineItem updateCartLineItem(int amount, CartLineItem existingCartItem, Product product) {
        int newQuantity = existingCartItem.getCartLineQuantity() + amount;
        existingCartItem.setCartLineQuantity(newQuantity);
        existingCartItem.setCartLinePrice(product.getPrice().multiply(BigDecimal.valueOf(newQuantity)));
        existingCartItem.setCartIndex(existingCartItem.getCartIndex());
        entityManager.merge(existingCartItem);
        return existingCartItem;
    }

    public Cart createCart(User user) {
        Cart newCart = new Cart();
        newCart.setUser(user);
        newCart.setCartLineItems(new HashSet<>());
        newCart.setTotalPrice(calculateCartTotalPrice(newCart));
        entityManager.persist(newCart);
        return newCart;
    }

    public Cart updateCart(Cart userCart) {
        userCart.setTotalPrice(calculateCartTotalPrice(userCart));
        entityManager.merge(userCart);
        return userCart;
    }


    public int generateCartIndex(Cart cart) {
        Integer lastCartIndex = cart.getCartLineItems().size();
        return lastCartIndex + 1;
    }

    public Cart getCartForAuthUser(String email) {
        String authenticatedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(authenticatedUser);
        if (user == null) {
            throw new UserNotFoundException("User not found for email: " + authenticatedUser);
        }
        Cart existingCart = entityManager.createQuery("SELECT c FROM Cart c JOIN FETCH c.user WHERE c.user = :user", Cart.class)
                .setParameter("user", user)
                .getResultList().stream().findFirst().orElse(null);

        if (existingCart == null) {
            existingCart = createCart(user);
        }
        return existingCart;
    }

    public boolean deleteCartAndItems(Long cartId) {
        entityManager.createQuery("DELETE FROM CartLineItem cli WHERE cli.cart.id = :cartId")
                .setParameter("cartId", cartId)
                .executeUpdate();
        entityManager.flush();
        entityManager.createQuery("UPDATE Cart c SET c.totalPrice = 0 WHERE c.id = :cartId")
                .setParameter("cartId", cartId)
                .executeUpdate();
        entityManager.createQuery("DELETE FROM Cart c WHERE c.id = :cartId AND c.totalPrice = 0")
                .setParameter("cartId", cartId)
                .executeUpdate();
        return true;
    }


    public Cart getCartByCartId(Long cartId) {
        return entityManager.find(Cart.class, cartId);
    }

    public BigDecimal calculateCartTotalPrice(Cart userCart) {
        if (userCart == null || userCart.getCartLineItems() == null) {
            return BigDecimal.ZERO;
        }

        return userCart.getCartLineItems().stream()
                .filter(Objects::nonNull)
                .map(CartLineItem::getCartLinePrice)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}




