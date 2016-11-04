package pl.training.cleanarchitecturetodo.presentation

import pl.training.cleanarchitecturetodo.application.MessageContent
import pl.training.cleanarchitecturetodo.application.MessageService
import pl.training.cleanarchitecturetodo.presentation.utils.SyncAsyncExecutor
import spock.lang.Specification

@SuppressWarnings("GroovyAssignabilityCheck")
class MessagesPresenterTest extends Specification {

    def messageService = Mock(MessageService)
    def presenter = new MessagesPresenter(new SyncAsyncExecutor(), messageService)

    def "loads messages just after ui attachment"() {
        given:
        def ui = Mock(MessagesPresenter.MessagesUI)
        def messages = Mock(List)
        messageService.loadMessages() >> messages

        when:
        presenter.attach(ui)

        then:
        1 * ui.showProgress()

        then:
        1 * ui.showMessages(messages)
    }

    def "sends message and refreshes list"() {
        given:
        def messageContent = "sample message"
        def ui = Mock(MessagesPresenter.MessagesUI)
        def messages = Mock(List)
        messageService.loadMessages() >> messages
        presenter.attach(ui)

        when:
        presenter.send(messageContent)

        then:
        1 * messageService.sendMessage(messageContent)
        1 * ui.showMessages(messages)
    }

    def "cancels message and refreshes list"() {
        given:
        def messageId = 1L
        def cancelableMessage = Mock(MessageContent) {
            getLocalId() >> messageId
            isCancelable() >> true
        }
        messageService.loadMessages() >> [cancelableMessage]

        def ui = Mock(MessagesPresenter.MessagesUI)
        presenter.attach(ui)

        when:
        presenter.cancel(cancelableMessage)

        then:
        messageService.cancelMessage(messageId)
        1 * ui.showMessages(_)
    }

    def "doesn't cancel not cancellable message"() {
        given:
        def notCancelableMessage = Mock(MessageContent) {
            isCancelable() >> false
        }
        messageService.loadMessages() >> [notCancelableMessage]

        def ui = Mock(MessagesPresenter.MessagesUI)
        presenter.attach(ui)

        when:
        presenter.cancel(notCancelableMessage)

        then:
        0 * messageService.cancelMessage(_)
        0 * ui.showMessages(_)
    }
}
