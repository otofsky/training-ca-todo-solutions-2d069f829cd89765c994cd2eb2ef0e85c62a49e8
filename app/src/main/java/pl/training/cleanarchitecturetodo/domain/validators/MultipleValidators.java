package pl.training.cleanarchitecturetodo.domain.validators;

import pl.training.cleanarchitecturetodo.domain.Message;
import pl.training.cleanarchitecturetodo.domain.MessageValidator;

public class MultipleValidators implements MessageValidator {
    private final MessageValidator[] validators;

    public MultipleValidators(MessageValidator... validators) {
        this.validators = validators;
    }

    @Override
    public void validate(Message message) {
        for (MessageValidator validator : validators) {
            validator.validate(message);
        }
    }
}
