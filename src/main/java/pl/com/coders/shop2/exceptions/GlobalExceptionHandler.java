package pl.com.coders.shop2.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value
            = {ProductWithGivenTitleExistsException.class})
    protected ResponseEntity<Object> handleTitleConflict(
            ProductWithGivenTitleExistsException ex, WebRequest request) {
        String bodyOfResponse = "Product with the given title already exists.";
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value
            = {ProductWithGivenTitleNotExistsException.class})
    protected ResponseEntity<Object> handleTitleNotFound(
            ProductWithGivenTitleNotExistsException ex, WebRequest request) {
        String bodyOfResponse = "Product with the given TITLE does not exist";
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value
            = {ProductWithGivenIdNotExistsException.class})
    protected ResponseEntity<Object> handleIdNotFound(
            ProductWithGivenIdNotExistsException ex, WebRequest request) {
        String bodyOfResponse = "Product with the given ID does not exist";
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

}
