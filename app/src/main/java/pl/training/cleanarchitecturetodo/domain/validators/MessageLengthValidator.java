package pl.training.cleanarchitecturetodo.domain.validators;

import java.util.Calendar;
import java.util.GregorianCalendar;

import pl.training.cleanarchitecturetodo.domain.Message;
import pl.training.cleanarchitecturetodo.domain.MessageInvalidException;
import pl.training.cleanarchitecturetodo.domain.MessageValidator;

public class MessageLengthValidator implements MessageValidator {

    @Override
    public void validate(Message message) {
        GregorianCalendar date = new GregorianCalendar();
        date.setTime(message.getSubmissionDate());
        if (date.get(Calendar.HOUR_OF_DAY) < 18 && message.getContent().length() > 140) {
            throw new MessageInvalidException("Message sent before 18:00 can have 140 chars tops");
        }
    }
}
