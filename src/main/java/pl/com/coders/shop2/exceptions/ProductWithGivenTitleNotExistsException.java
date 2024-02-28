package pl.com.coders.shop2.exceptions;

public class ProductWithGivenTitleNotExistsException extends RuntimeException{
    public ProductWithGivenTitleNotExistsException(String message) {
        super(message);
    }
}
