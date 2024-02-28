package pl.com.coders.shop2.exceptions;

public class InsufficientStockException extends Throwable {
    public InsufficientStockException(String message) {
        super(message);
    }
}
