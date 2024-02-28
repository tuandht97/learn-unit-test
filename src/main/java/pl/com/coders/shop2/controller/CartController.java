package pl.com.coders.shop2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.com.coders.shop2.domain.dto.CartDto;
import pl.com.coders.shop2.exceptions.ProductNotFoundException;
import pl.com.coders.shop2.service.CartService;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/carts")
@CrossOrigin(origins = "http://localhost:4200")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/{productTitle}/{amount}/addProductToCart")
    public ResponseEntity<CartDto> addProductToCart(@PathVariable String productTitle,@PathVariable int amount) throws ProductNotFoundException {
        productTitle = URLDecoder.decode(productTitle, StandardCharsets.UTF_8);
        CartDto cartDto = cartService.addProductToCart(productTitle, amount);
        return ResponseEntity.status(HttpStatus.OK).body(cartDto);
    }

    @DeleteMapping("/{cartIndex}/{id}")
    public ResponseEntity<String> deleteCartByIndex(@PathVariable int cartIndex, @PathVariable Long id) {
        cartService.deleteByIndex(id, cartIndex);
        return ResponseEntity.status(HttpStatus.OK).body("Cart was deleted");
    }

    @GetMapping("/getByEmail/{userEmail}")
    public ResponseEntity<CartDto> getCartsForAuthUser(@PathVariable String userEmail) {
        CartDto cartForAuthUser = cartService.getCartForAuthUser(userEmail);
        return  ResponseEntity.status(HttpStatus.OK).body(cartForAuthUser);
    }

}



