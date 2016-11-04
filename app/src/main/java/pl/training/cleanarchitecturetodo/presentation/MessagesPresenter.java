package pl.training.cleanarchitecturetodo.presentation;

import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import pl.training.cleanarchitecturetodo.application.MessageContent;
import pl.training.cleanarchitecturetodo.application.MessageService;
import pl.training.cleanarchitecturetodo.presentation.AsyncExecutor.ResultCallback;
import pl.training.cleanarchitecturetodo.presentation.AsyncExecutor.Task;

public class MessagesPresenter extends BasePresenter<MessagesPresenter.MessagesUI> {
    private final AsyncExecutor asyncExecutor;
    private final MessageService messageService;

    @Inject
    public MessagesPresenter(AsyncExecutor asyncExecutor, MessageService messageService) {
        this.asyncExecutor = asyncExecutor;
        this.messageService = messageService;
    }

    @Override
    public void attach(MessagesUI messagesUI) {
        super.attach(messagesUI);
        execute(new ShowProgressCmd());
        loadMessages();
    }

    public void send(final String content) {
        asyncExecutor.submit(new Task<Void>() {
            @Override
            public Void call() {
                messageService.sendMessage(content);
                return null;
            }
        }, new ResultCallback<Void>() {
            @Override
            public void onComplete(Void aVoid) {
                loadMessages();
            }

            @Override
            public void onError(Exception ex) {

                Log.d("OnError", "onError: ");
                 execute(new ShowErrorCmd(ex.getMessage()));
            }
        });
    }

    private void loadMessages() {
        asyncExecutor.submit(new Task<List<MessageContent>>() {
            @Override
            public List<MessageContent> call() {
                return messageService.loadMessages();
            }
        }, new ResultCallback<List<MessageContent>>() {
            @Override
            public void onComplete(List<MessageContent> result) {
                execute(new ShowMessagesCmd(result));
            }

            @Override
            public void onError(Exception ex) {
                execute(new ShowErrorCmd(ex.getMessage()));
            }
        });
    }

    public void cancel(final MessageContent messageToCancel) {
        if (messageToCancel.isCancelable()) {
            asyncExecutor.submit(new Task<Void>() {
                @Override
                public Void call() {
                    messageService.cancelMessage(messageToCancel.getLocalId());
                    return null;
                }
            }, new ResultCallback<Void>() {
                @Override
                public void onComplete(Void aVoid) {
                    loadMessages();
                }

                @Override
                public void onError(Exception ex) {
                    execute(new ShowErrorCmd(ex.getMessage()));
                }
            });
        }
    }

    public interface MessagesUI {
        void showProgress();

        void showMessages(List<MessageContent> messages);

        void showError(String message);
    }

    private static class ShowMessagesCmd implements UICommand<MessagesUI> {
        private final List<MessageContent> messages;

        private ShowMessagesCmd(List<MessageContent> messages) {
            this.messages = messages;
        }

        @Override
        public void execute(MessagesUI messagesUI) {
            messagesUI.showMessages(messages);
        }
    }

    private static class ShowErrorCmd implements UICommand<MessagesUI> {
        private final String message;

        public ShowErrorCmd(String message) {
            this.message = message;
        }

        @Override
        public void execute(MessagesUI messagesUI) {

            messagesUI.showError(message);
        }
    }

    private static class ShowProgressCmd implements UICommand<MessagesUI> {
        @Override
        public void execute(MessagesUI messagesUI) {
            messagesUI.showProgress();
        }
    }
}
