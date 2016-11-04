package pl.training.cleanarchitecturetodo.domain.validators

import pl.training.cleanarchitecturetodo.domain.Message
import pl.training.cleanarchitecturetodo.domain.MessageInvalidException
import pl.training.cleanarchitecturetodo.domain.MessageValidator
import spock.lang.Specification

class MultipleValidatorsSpec extends Specification {
    def "executes all validators when no exception is thrown"() {
        given:
        def validator1 = Mock(MessageValidator)
        def validator2 = Mock(MessageValidator)
        def validator = new MultipleValidators(validator1, validator2)
        def message = Mock(Message)

        when:
        validator.validate(message)

        then:
        1 * validator1.validate(message)
        1 * validator2.validate(message)
    }

    def "when first validator throws the rest is not executed"() {
        given:
        def validator1 = Mock(MessageValidator)
        def validator2 = Mock(MessageValidator)
        def validator = new MultipleValidators(validator1, validator2)
        def message = Mock(Message)

        when:
        validator.validate(message)

        then:
        thrown(MessageInvalidException)
        1 * validator1.validate(message) >> { throw new MessageInvalidException("") }
        0 * validator2.validate(message)
    }
}
