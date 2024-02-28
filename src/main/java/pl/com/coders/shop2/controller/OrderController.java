package pl.com.coders.shop2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.com.coders.shop2.domain.dto.OrderDto;
import pl.com.coders.shop2.exceptions.EmptyCartException;
import pl.com.coders.shop2.service.OrderService;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("saveOrder/{userEmail}")
    public ResponseEntity<OrderDto> saveOrder(@PathVariable String userEmail){
        OrderDto createdOrder = orderService.createOrderFromCart(userEmail);
        if (!createdOrder.equals(null)) {
            return ResponseEntity.status(HttpStatus.OK).body(createdOrder);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/byUser/{userEmail}")
    public ResponseEntity<OrderDto> getOrdersByUser(@PathVariable String userEmail) throws EmptyCartException {
        OrderDto orderByUser = orderService.getOrdersByUser(userEmail);
        return ResponseEntity.status(HttpStatus.OK).body(orderByUser);
    }
}
