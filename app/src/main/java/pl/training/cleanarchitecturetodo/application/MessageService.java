package pl.training.cleanarchitecturetodo.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import pl.training.cleanarchitecturetodo.application.RemoteClientFacade.RemoteMessage;
import pl.training.cleanarchitecturetodo.domain.Message;
import pl.training.cleanarchitecturetodo.domain.MessageFactory;
import pl.training.cleanarchitecturetodo.domain.MessageRepository;
import pl.training.cleanarchitecturetodo.domain.TimeProvider;

public class MessageService {
    private final TimeProvider timeProvider;
    private final MessageFactory messageFactory;
    private final MessageRepository messageRepository;
    private final RemoteClientFacade remoteClientFacade;
    private final DelayedExecutor delayedExecutor;

    @Inject
    public MessageService(MessageFactory messageFactory, MessageRepository messageRepository, RemoteClientFacade remoteClientFacade, TimeProvider timeProvider, DelayedExecutor delayedExecutor) {
        this.messageFactory = messageFactory;
        this.messageRepository = messageRepository;
        this.remoteClientFacade = remoteClientFacade;
        this.timeProvider = timeProvider;
        this.delayedExecutor = delayedExecutor;
    }

    public void sendMessage(String content) {
        final Message message = messageFactory.createMessage(content);
        messageRepository.save(message);
        delayedExecutor.executeDelayed(new DelayedSendRunnable(message.getId()), TimeUnit.SECONDS.toMillis(5));
    }

    public void cancelMessage(long messageId) {
        Message message = messageRepository.load(messageId);
        message.cancel();
        messageRepository.save(message);
    }

    public List<MessageContent> loadMessages() {
        List<RemoteMessage> remoteMessages = remoteClientFacade.loadMessages();
        List<Message> pendingMessages = messageRepository.loadPending();

        List<MessageContent> messages = new ArrayList<>(remoteMessages.size() + pendingMessages.size());
        for (RemoteMessage remoteMessage : remoteMessages) {
            messages.add(new MessageContent(null, remoteMessage.getContent(), remoteMessage.getPublishDate(), false));
        }

        for (Message pendingMessage : pendingMessages) {
            messages.add(new MessageContent(pendingMessage.getId(), pendingMessage.getContent(), pendingMessage.getSubmissionDate(), true));
        }

        Collections.sort(messages);
        Collections.reverse(messages);

        return Collections.unmodifiableList(messages);
    }

    private class DelayedSendRunnable implements Runnable {
        private final Long messageId;

        public DelayedSendRunnable(Long id) {
            messageId = id;
        }

        @Override
        public void run() {
            Message message = messageRepository.load(messageId);
            if (message != null && message.isPending()) {
                remoteClientFacade.send(message);
                message.send(timeProvider);
                messageRepository.save(message);
            }
        }
    }
}
