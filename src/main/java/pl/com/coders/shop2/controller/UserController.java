package pl.com.coders.shop2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.com.coders.shop2.domain.dto.UserDto;
import pl.com.coders.shop2.service.UserService;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody UserDto userDto) {
        UserDto createdUser = userService.create(userDto);
        return ResponseEntity.status(HttpStatus.OK).body(createdUser);
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserDto> findByEmail(@PathVariable String email) {
        UserDto user = userService.findByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping("/byId/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable Long userId) {
        UserDto user = userService.findById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
