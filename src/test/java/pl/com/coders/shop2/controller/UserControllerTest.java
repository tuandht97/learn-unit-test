package pl.com.coders.shop2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.com.coders.shop2.domain.User;
import pl.com.coders.shop2.domain.dto.UserDto;
import pl.com.coders.shop2.service.UserService;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;
    private UserDto userDto;
    private User user;

    @BeforeEach
    void setUp() {
        userDto = new UserDto("john@example.com", "John", "Doe", "pass1");
        user = new User("john@example.com", "John", "Doe", "pass1");
    }

    @Test
    @DisplayName("create User")
    void create() throws Exception {
        when(userService.create(any())).thenReturn(userDto);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andReturn();
        String responseContent = result.getResponse().getContentAsString();
        UserDto responseUser = objectMapper.readValue(responseContent, UserDto.class);
        assertEquals(userDto.getEmail(), responseUser.getEmail());
        verify(userService, times(1)).create(any());
    }

    @Test
    void findByEmail() throws Exception {
        String userEmail = "john@example.com";
        when(userService.findByEmail(userEmail)).thenReturn(userDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/{userEmail}", userEmail))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        if (!jsonResponse.isEmpty()) {
            UserDto responseUser = objectMapper.readValue(jsonResponse, UserDto.class);
        }
        assertNotNull(result.getResponse().getContentType());
    }
}