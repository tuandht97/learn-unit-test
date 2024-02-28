package pl.com.coders.shop2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.com.coders.shop2.domain.Cart;
import pl.com.coders.shop2.domain.CartLineItem;
import pl.com.coders.shop2.domain.Product;
import pl.com.coders.shop2.domain.User;
import pl.com.coders.shop2.domain.dto.CartDto;
import pl.com.coders.shop2.exceptions.ProductNotFoundException;
import pl.com.coders.shop2.mapper.CartMapper;
import pl.com.coders.shop2.mapper.UserMapper;
import pl.com.coders.shop2.repository.CartRepository;
import pl.com.coders.shop2.repository.ProductRepository;
import pl.com.coders.shop2.repository.UserRepository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;
    private final UserMapper userMapper;

    public CartDto addProductToCart(String productTitle, int amount)
            throws ProductNotFoundException {
        String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(loggedUser);
        Product product = getProduct(productTitle);
        Cart userCart = getOrCreateUserCart(loggedUser);
        CartLineItem existingCartItem = findCartItem(userCart, product);
        if (existingCartItem == null) {
            cartRepository.createCartLineItem(amount, userCart, product);
        } else {
            if (product.getQuantity() >= amount) {
                cartRepository.updateCartLineItem(amount, existingCartItem, product);
            }
            updateAfterAddingItem(existingCartItem, amount);
        }
        productRepository.update(product, product.getId());
        cartRepository.updateCart(userCart);
        return cartMapper.toDto(userCart);
    }


    public Cart getOrCreateUserCart(String userEmail) {
        Cart userCart = cartRepository.getCartForAuthUser(userEmail);
        if (userCart == null) {
            userCart = cartRepository.createCart(new User());
        }
        return userCart;
    }

    private Product getProduct(String productTitle) throws ProductNotFoundException {
        return productRepository.getProductByName(productTitle);
    }

    private CartLineItem findCartItem(Cart cart, Product product) {
        if (cart != null && cart.getCartLineItems() != null) {
            return cart.getCartLineItems().stream()
                    .filter(cli -> Objects.equals(cli.getProduct().getId(), product.getId()))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    public boolean deleteByIndex(Long cartId, int cartIndex) {
        Cart cart = cartRepository.getCartByCartId(cartId);

        if (cart != null && cart.getCartLineItems() != null) {
            Optional<CartLineItem> cartLineItemOptional = cart.getCartLineItems().stream()
                    .filter(item -> item.getCartIndex() == cartIndex)
                    .findFirst();

            cartLineItemOptional.ifPresent(cartLineItem -> {
                cart.getCartLineItems().remove(cartLineItem);
                updateCartAfterItemRemoval(cart, cartLineItem);
                cart.setTotalPrice(cartRepository.calculateCartTotalPrice(cart));
                cartRepository.updateCart(cart);
            });

            if (cart != null
                    && (cart.getCartLineItems() == null || cart.getCartLineItems().isEmpty())
                    && (cart.getTotalPrice() == null || cart.getTotalPrice().compareTo(BigDecimal.ZERO) == 0)) {
                cartRepository.deleteCartAndItems(cartId);
            }
        }
        return false;
    }

    public CartDto getCartForAuthUser(String userEmail) {
        userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Cart cartForUser = cartRepository.getCartForAuthUser(userEmail);
        return cartMapper.toDto(cartForUser);
    }

    private void updateCartAfterItemRemoval(Cart cart, CartLineItem removedItem) {
        Product product = removedItem.getProduct();
        product.setQuantity(product.getQuantity() + removedItem.getCartLineQuantity());

        cart.setTotalPrice(cartRepository.calculateCartTotalPrice(cart));
        updateCartIndex(cart, removedItem.getCartIndex());
    }

    private void updateAfterAddingItem(CartLineItem addedItem, int amount) {
        if (addedItem != null) {
            Product product = addedItem.getProduct();
            int remainingQuantity = Math.max(product.getQuantity() - amount, 0);
            product.setQuantity(remainingQuantity);
        }
    }

    private void updateCartIndex(Cart cart, int deletedIndex) {
        cart.getCartLineItems().stream()
                .filter(item -> item.getCartIndex() > deletedIndex)
                .forEach(cartItem -> cartItem.setCartIndex(cartItem.getCartIndex() - 1));
    }
}


