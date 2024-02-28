package pl.com.coders.shop2.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pl.com.coders.shop2.domain.User;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("john@example.com", "John", "Doe", "pass1");
    }

    @Test
    void createAndFindById() {
        User createdUser = userRepository.create(user);
        assertNotNull(createdUser.getId());
        User foundUser = userRepository.findById(createdUser.getId());
        assertNotNull(foundUser);
        assertEquals(createdUser.getId(), foundUser.getId());
        assertEquals(createdUser.getEmail(), foundUser.getEmail());
    }

    @Test
    void findByEmail() {
        String userEmail = "john@example.com";
        createAndFindById();
        User foundUserByEmail = userRepository.findByEmail(userEmail);
        assertNotNull(foundUserByEmail);
    }
}