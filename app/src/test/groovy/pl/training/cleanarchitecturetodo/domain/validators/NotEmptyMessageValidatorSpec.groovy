package pl.training.cleanarchitecturetodo.domain.validators

import pl.training.cleanarchitecturetodo.domain.Message
import pl.training.cleanarchitecturetodo.domain.MessageInvalidException
import spock.lang.Specification

class NotEmptyMessageValidatorSpec extends Specification {

    def "not empty message is correct"() {
        given:
        def message = new Message("content", new Date())

        when:
        validator().validate(message)

        then:
        notThrown(MessageInvalidException)
    }

    def "null message is incorrect"() {
        given:
        def message = new Message(null, new Date())

        when:
        validator().validate(message)

        then:
        thrown(MessageInvalidException)
    }

    def "empty message is incorrect"() {
        given:
        def message = new Message("", new Date())

        when:
        validator().validate(message)

        then:
        thrown(MessageInvalidException)
    }

    def NotEmptyMessageValidator validator() {
        new NotEmptyMessageValidator()
    }
}
