package pl.training.cleanarchitecturetodo.domain

import spock.lang.Specification

import static pl.training.cleanarchitecturetodo.domain.Message.State.*

class MessageSpec extends Specification {
    private static final String DEFAULT_CONTENT = "sample content"
    private static final Date DEFAULT_SUBMISSION_DATE = new Date()

    def timeProvider = Mock(TimeProvider)

    def "creates message with pending state"() {
        expect:
        createDefaultMessage().getState() == PENDING
    }

    def "creates message with content and submission date"() {
        given:
        def content = "sample content"
        def submissionDate = new Date()

        when:
        def message = createMessage(content, submissionDate)

        then:
        message.getContent() == content
        message.getSubmissionDate() == submissionDate
    }

    def "canceled message is canceled"() {
        given:
        def message = createDefaultMessage()

        when:
        message.cancel()

        then:
        message.getState() == CANCELED
        !message.isPending()
    }

    def "message cannot be canceled twice"() {
        given:
        def message = createDefaultMessage()
        message.cancel()

        when:
        message.cancel()

        then:
        thrown(MessageStateChangeException)
        message.getState() == CANCELED
    }

    def "send message is send"() {
        given:
        def message = createDefaultMessage()

        when:
        sendMessage(message)

        then:
        message.getState() == SENT
        !message.isPending()
    }

    def "message cannot be sent twice"() {
        given:
        def message = createDefaultMessage()
        message.send(timeProvider)

        when:
        sendMessage(message)

        then:
        thrown(MessageStateChangeException)
        message.getState() == SENT
    }

    def "cancelled message cannot be sent"() {
        given:
        def message = createDefaultMessage()
        message.cancel()

        when:
        sendMessage(message)

        then:
        thrown(MessageStateChangeException)
        message.getState() == CANCELED
    }

    def "send message cannot be cancelled"() {
        given:
        def message = createDefaultMessage()
        sendMessage(message)

        when:
        message.cancel()

        then:
        thrown(MessageStateChangeException)
        message.getState() == SENT
    }

    private sendMessage(Message message) {
        message.send(timeProvider)
    }

    def "send message has send date"() {
        given:
        def message = createDefaultMessage()
        def sendTime = new Date()
        timeProvider.getCurrentTime() >> sendTime

        when:
        sendMessage(message)

        then:
        message.getSendTime() == sendTime
    }

    def "not send message has no send time"() {
        given:
        def message = createDefaultMessage()

        expect:
        message.getSendTime() == null
    }

    def "message has not ID after creation"() {
        given:
        def message = createDefaultMessage()

        expect:
        message.getId() == null
    }

    def "message has id after settings id"() {
        given:
        def message = createDefaultMessage()
        message.setId(1L)

        expect:
        message.getId() == 1L
    }

    def "just created messages should be equal"() {
        expect:
        createDefaultMessage().equals(createDefaultMessage())
    }

    def Message createDefaultMessage() {
        new Message(DEFAULT_CONTENT, DEFAULT_SUBMISSION_DATE);
    }

    def Message createMessage(String content, Date submissionDate) {
        new Message(content, submissionDate)
    }
}
