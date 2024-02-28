package pl.com.coders.shop2.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import pl.com.coders.shop2.domain.User;
import pl.com.coders.shop2.domain.dto.UserDto;
import pl.com.coders.shop2.mapper.UserMapper;
import pl.com.coders.shop2.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void testCreate() {
        // Arrange
        UserDto userDto = new UserDto("john@example.com", "John", "Doe", "pass1");
        User user = new User("john@example.com", "John", "Doe", "pass1");

        when(userMapper.dtoToUser(userDto)).thenReturn(user);
        when(userRepository.create(user)).thenReturn(user);
        when(userMapper.userToDto(user)).thenReturn(userDto);

        // Act
        UserDto createdUserDto = userService.create(userDto);

        // Assert
        assertEquals(userDto, createdUserDto);
        verify(userMapper, times(1)).dtoToUser(userDto);
        verify(userRepository, times(1)).create(user);
        verify(userMapper, times(1)).userToDto(user);
    }

    @Test
    void testFindByEmail() {
        // Arrange
        String email = "john@example.com";
        User user = new User(email, "John", "Doe", "pass1");
        UserDto userDto = new UserDto(email, "John", "Doe", "pass1");

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(userMapper.userToDto(user)).thenReturn(userDto);

        // Act
        UserDto foundUserDto = userService.findByEmail(email);

        // Assert
        assertEquals(userDto, foundUserDto);
        verify(userRepository, times(1)).findByEmail(email);
        verify(userMapper, times(1)).userToDto(user);
    }

    @Test
    void testFindById() {
        // Arrange
        Long userId = 1L;
        User user = new User("john@example.com", "John", "Doe", "pass1");
        UserDto userDto = new UserDto("john@example.com", "John", "Doe", "pass1");

        when(userRepository.findById(userId)).thenReturn(user);
        when(userMapper.userToDto(user)).thenReturn(userDto);

        // Act
        UserDto foundUserDto = userService.findById(userId);

        // Assert
        assertEquals(userDto, foundUserDto);
        verify(userRepository, times(1)).findById(userId);
        verify(userMapper, times(1)).userToDto(user);
    }
}
