package pl.training.cleanarchitecturetodo.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import pl.training.cleanarchitecturetodo.api.model.NewMessage;
import pl.training.cleanarchitecturetodo.api.model.RemoteMessageData;
import pl.training.cleanarchitecturetodo.application.RemoteClientFacade;
import pl.training.cleanarchitecturetodo.domain.Message;

public class RemoteClientFacadeImpl implements RemoteClientFacade {
    private final WallAPI wallAPI;

    @Inject
    public RemoteClientFacadeImpl(WallAPI wallAPI) {
        this.wallAPI = wallAPI;
    }

    @Override
    public void send(Message message) {
        try {
            wallAPI.sendMessage(new NewMessage(message.getContent())).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<RemoteMessage> loadMessages() {
        try {
            List<RemoteMessageData> messages = wallAPI.getAllMessages().execute().body();
            ArrayList<RemoteMessage> msgs = new ArrayList<>(messages.size());
            for (RemoteMessageData message : messages) {
                msgs.add(new RemoteMessage(message.getContent(), message.getPublicationDate()));
            }
            return msgs;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
