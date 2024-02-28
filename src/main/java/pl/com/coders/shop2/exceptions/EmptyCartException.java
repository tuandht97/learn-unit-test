package pl.com.coders.shop2.exceptions;

public class EmptyCartException extends Throwable {
    public EmptyCartException(String message) {
        super(message);
    }
}
