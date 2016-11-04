package pl.training.cleanarchitecturetodo.domain;

public class MessageStateChangeException extends RuntimeException {
    public MessageStateChangeException(String message) {
        super(message);
    }
}
