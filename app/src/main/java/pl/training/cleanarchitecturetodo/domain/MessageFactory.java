package pl.training.cleanarchitecturetodo.domain;

import javax.inject.Inject;

public class MessageFactory {
    private final MessageValidator validator;
    private final TimeProvider timeProvider;

    @Inject
    public MessageFactory(MessageValidator validator, TimeProvider timeProvider) {
        this.validator = validator;
        this.timeProvider = timeProvider;
    }

    public Message createMessage(String content) {
        Message message = new Message(content, timeProvider.getCurrentTime());
        validator.validate(message);
        return message;
    }
}
