package pl.training.cleanarchitecturetodo.domain

import spock.lang.Specification

class MessageFactorySpec extends Specification {

    def validator = Mock(MessageValidator)
    def timeProvider = Mock(TimeProvider)
    def factory = new MessageFactory(validator, timeProvider)

    def "creates valid message"() {
        when:
        def message = factory.createMessage("content")

        then:
        notThrown(MessageInvalidException)
        message != null
        1 * validator.validate(_)
    }

    def "throws when message is invalid"() {
        given:
        validator.validate(_) >> { throw new MessageInvalidException("") }

        when:
        factory.createMessage("content")

        then:
        thrown(MessageInvalidException)
    }
}
