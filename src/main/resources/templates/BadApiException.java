package {{package}}.exception;

public class BadApiException extends RuntimeException {
    public BadApiException(String message) {
        super(message);
    }
}
