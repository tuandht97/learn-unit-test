package pl.com.coders.shop2.exceptions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.MockitoAnnotations.openMocks;

@SpringBootTest
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private ProductWithGivenTitleExistsException titleExistsException;

    @Mock
    private ProductWithGivenTitleNotExistsException titleNotExistsException;

    @Mock
    private ProductWithGivenIdNotExistsException idNotExistsException;

    @Mock
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void handleTitleConflict_shouldReturnConflictResponse() {
        String bodyOfResponse = "Product with the given title already exists.";

        // Sử dụng ReflectionTestUtils để truy cập phương thức handleExceptionInternal
        Object result = ReflectionTestUtils.invokeMethod(
                globalExceptionHandler,
                "handleExceptionInternal",
                titleExistsException, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, webRequest
        );

        assertEquals(HttpStatus.CONFLICT, ((ResponseEntity<?>) result).getStatusCode());
        assertEquals(bodyOfResponse, ((ResponseEntity<?>) result).getBody());
    }

    @Test
    void handleTitleNotFound_shouldReturnNotFoundResponse() {
        String bodyOfResponse = "Product with the given TITLE does not exist";

        // Sử dụng ReflectionTestUtils để truy cập phương thức handleExceptionInternal
        Object result = ReflectionTestUtils.invokeMethod(
                globalExceptionHandler,
                "handleExceptionInternal",
                titleNotExistsException, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, webRequest
        );

        assertEquals(HttpStatus.NOT_FOUND, ((ResponseEntity<?>) result).getStatusCode());
        assertEquals(bodyOfResponse, ((ResponseEntity<?>) result).getBody());
    }

    @Test
    void handleIdNotFound_shouldReturnNotFoundResponse() {
        String bodyOfResponse = "Product with the given ID does not exist";

        // Sử dụng ReflectionTestUtils để truy cập phương thức handleExceptionInternal
        Object result = ReflectionTestUtils.invokeMethod(
                globalExceptionHandler,
                "handleExceptionInternal",
                idNotExistsException, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, webRequest
        );

        assertEquals(HttpStatus.NOT_FOUND, ((ResponseEntity<?>) result).getStatusCode());
        assertEquals(bodyOfResponse, ((ResponseEntity<?>) result).getBody());
    }
}
