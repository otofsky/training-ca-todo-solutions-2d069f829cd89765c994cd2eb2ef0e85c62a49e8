package pl.training.cleanarchitecturetodo.domain.validators;

import pl.training.cleanarchitecturetodo.domain.Message;
import pl.training.cleanarchitecturetodo.domain.MessageInvalidException;
import pl.training.cleanarchitecturetodo.domain.MessageValidator;

public class NotEmptyMessageValidator implements MessageValidator {
    @Override
    public void validate(Message message) {
        if (message.getContent() == null || message.getContent().isEmpty()) {
            throw new MessageInvalidException("Message content cannot be empty or null");
        }
    }
}
