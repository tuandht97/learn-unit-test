package pl.com.coders.shop2.exceptions;

public class ProductWithGivenTitleExistsException extends RuntimeException {
    public ProductWithGivenTitleExistsException(String message) {
        super(message);
    }
}