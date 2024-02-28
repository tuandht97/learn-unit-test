package pl.com.coders.shop2.exceptions;

public class ProductWithGivenIdNotExistsException extends RuntimeException {
    public ProductWithGivenIdNotExistsException(String message) {
        super(message);
    }
}