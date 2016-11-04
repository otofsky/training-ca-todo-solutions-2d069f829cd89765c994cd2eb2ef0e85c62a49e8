package pl.training.cleanarchitecturetodo.domain;

public class MessageInvalidException extends RuntimeException {
    public MessageInvalidException(String message) {
        super(message);
    }
}
