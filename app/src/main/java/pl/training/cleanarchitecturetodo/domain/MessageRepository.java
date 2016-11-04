package pl.training.cleanarchitecturetodo.domain;

import java.util.List;

public interface MessageRepository {
    void save(Message message);

    Message load(Long id);

    List<Message> loadPending();
}
