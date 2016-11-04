package pl.training.cleanarchitecturetodo.application

import pl.training.cleanarchitecturetodo.domain.Message
import pl.training.cleanarchitecturetodo.domain.MessageFactory
import pl.training.cleanarchitecturetodo.domain.MessageRepository
import pl.training.cleanarchitecturetodo.domain.TimeProvider
import spock.lang.Specification

@SuppressWarnings("GroovyAssignabilityCheck")
class MessageServiceSpec extends Specification {

    def message = Mock(Message)
    def remoteClient = Mock(RemoteClientFacade)
    def repository = Mock(MessageRepository)
    def factory = Mock(MessageFactory)
    def timeProvider = Mock(TimeProvider)
    def service = givenService(immediateExecutor())


    void setup() {
        factory.createMessage(_) >> message
        repository.load(_) >> message
    }

    def "saves pending and sent message in repository"() {
        given:
        message.isPending() >> true

        when:
        sendMessage()

        then:
        2 * repository.save(message)
    }

    def "sends message using remote client"() {
        given:
        message.isPending() >> true

        when:
        sendMessage()

        then:
        1 * remoteClient.send(message)
    }

    def "marks message as sent"() {
        given:
        message.isPending() >> true

        when:
        sendMessage()

        then:
        1 * message.send(timeProvider)
    }

    def "when remote client fails sending message is saved as pending"() {
        given:
        message.isPending() >> true
        remoteClient.send(_) >> { throw new IllegalStateException("") }

        when:
        sendMessage()

        then:
        thrown(IllegalStateException)
        1 * repository.save(message)
        0 * message.send(timeProvider)
    }

    def "doesn't send message when it is not pending"() {
        given:
        message.isPending() >> false

        when:
        sendMessage()

        then:
        0 * message.send(timeProvider)
        0 * remoteClient.send(message)
    }

    def "cancells message by id"() {
        when:
        service.cancelMessage(1L)

        then:
        message.cancel()
        1 * repository.save(message)
    }

    def "loads messages by fetching them from remote server"() {
        given:
        def remoteMessages = [givenRemoteMessage(), givenRemoteMessage()]
        remoteClient.loadMessages() >> remoteMessages
        repository.loadPending() >> []

        when:
        List<MessageContent> messages = service.loadMessages()

        then:
        messages.size() == 2
        messages*.content.containsAll(remoteMessages*.content)
    }

    def "loads pending messages from local repository"() {
        given:
        def pendingMessages = [givenPendingMessage(), givenPendingMessage()]
        repository.loadPending() >> pendingMessages

        remoteClient.loadMessages() >> []

        when:
        List<MessageContent> messages = service.loadMessages()

        then:
        messages.size() == 2
        messages*.content.containsAll(pendingMessages*.content)
    }

    def "loaded pending message should be cancellable and have local id"() {
        given:
        def pendingMessages = [givenPendingMessage()]
        repository.loadPending() >> pendingMessages
        remoteClient.loadMessages() >> []

        when:
        List<MessageContent> messages = service.loadMessages()

        then:
        messages.every { it.cancelable && it.localId != null }
    }

    def "loaded remote messages should not be cancellable and should not have local id"() {
        given:
        def remoteMessages = [givenRemoteMessage()]
        remoteClient.loadMessages() >> remoteMessages
        repository.loadPending() >> []

        when:
        List<MessageContent> messages = service.loadMessages()

        then:
        messages.every { !it.cancelable && it.localId == null }
    }

    def RemoteClientFacade.RemoteMessage givenRemoteMessage() {
        new RemoteClientFacade.RemoteMessage("sample content", new Date());
    }

    def Message givenPendingMessage() {
        def message = new Message("content", new Date())
        message.setId(new Random().nextLong())
        message
    }

    def sendMessage() {
        service.sendMessage("sample content")
    }

    def MessageService givenService(DelayedExecutor executor) {
        new MessageService(factory, repository, remoteClient, timeProvider, executor)
    }

    def DelayedExecutor immediateExecutor() {
        return { Runnable r, long delay -> r.run() }
    }
}
