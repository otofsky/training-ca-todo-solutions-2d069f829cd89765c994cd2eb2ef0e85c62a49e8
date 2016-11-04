package pl.training.cleanarchitecturetodo.domain.validators

import pl.training.cleanarchitecturetodo.domain.Message
import pl.training.cleanarchitecturetodo.domain.MessageInvalidException
import spock.lang.Specification

class MessageLengthValidatorSpec extends Specification {

    def "before 18:00 message can have content shorter than 140 chars"() {
        given:
        def message = new Message(shortContent(), before6pm())

        when:
        validator().validate(message)

        then:
        notThrown(MessageInvalidException)
    }

    def "before 18:00 message cannot have content longer than 140 chars"() {
        given:
        def message = new Message(longContent(), before6pm())

        when:
        validator().validate(message)

        then:
        thrown(MessageInvalidException)
    }

    def "after 18 content can be long"() {
        given:
        def message = new Message(longContent(), after6pm())

        when:
        validator().validate(message)

        then:
        notThrown(MessageInvalidException)
    }

    def String longContent() {
        def builder = new StringBuilder();
        for (i in 1..150) {
            builder.append("a")
        }
        builder.toString()
    }

    def String shortContent() {
        "shortContent"
    }

    def MessageLengthValidator validator() {
        new MessageLengthValidator()
    }

    def Date before6pm() {
        def calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 17)
        calendar.set(Calendar.MINUTE, 59);
        calendar.getTime()
    }

    def Date after6pm() {
        def calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 18)
        calendar.set(Calendar.MINUTE, 1);
        calendar.getTime()
    }
}
