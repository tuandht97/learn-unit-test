package pl.com.coders.shop2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.coders.shop2.domain.User;
import pl.com.coders.shop2.domain.dto.UserDto;
import pl.com.coders.shop2.mapper.UserMapper;
import pl.com.coders.shop2.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final UserMapper userMapper;

    public UserDto create(UserDto userDto) {
        User user = userMapper.dtoToUser(userDto);
        User savedUser = userRepository.create(user);
        return userMapper.userToDto(savedUser);
    }

    public UserDto findByEmail(String email) {
        User byEmail = userRepository.findByEmail(email);
        return userMapper.userToDto(byEmail);
    }

    public UserDto findById(Long userId) {
        User byEmail = userRepository.findById(userId);
        return userMapper.userToDto(byEmail);
    }
}
