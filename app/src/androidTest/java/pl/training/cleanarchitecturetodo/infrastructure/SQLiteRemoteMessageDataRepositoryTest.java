package pl.training.cleanarchitecturetodo.infrastructure;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.RenamingDelegatingContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import pl.training.cleanarchitecturetodo.domain.Message;
import pl.training.cleanarchitecturetodo.domain.MessageRepository;
import pl.training.cleanarchitecturetodo.domain.TimeProvider;
import pl.training.cleanarchitecturetodo.persistance.SQLiteMessageRepository;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(AndroidJUnit4.class)
public class SQLiteRemoteMessageDataRepositoryTest {

    private static final String MESSAGE_CONTENT = "sample content";
    private static final Date SUBMISSION_DATE = new Date();
    private MessageRepository repository;

    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        repository = new SQLiteMessageRepository(new RenamingDelegatingContext(context, "test_"));
    }

    @Test
    public void sendsMessageIdDuringSave() throws Exception {
        Message message = createDefaultMessage();

        repository.save(message);

        assertThat(message.getId()).isNotNull();
    }

    @Test
    public void savedMessageShouldBeLoadedById() throws Exception {
        Message message = createDefaultMessage();
        repository.save(message);

        Message loadedMessage = repository.load(message.getId());
        assertThat(loadedMessage).isEqualTo(message);
    }

    @Test
    public void savingExistingMessageUpdatesPreviousOne() throws Exception {
        Message message = createDefaultMessage();

        repository.save(message);
        long firstSaveId = message.getId();

        repository.save(message);
        long secondSaveId = message.getId();

        assertThat(firstSaveId).isEqualTo(secondSaveId);
    }

    @Test
    public void loadsUpdatedMessageAfterSavingUpdate() throws Exception {
        Message message = createDefaultMessage();
        repository.save(message);

        message.cancel();
        repository.save(message);

        Message loadedMessage = repository.load(message.getId());
        assertThat(loadedMessage).isEqualTo(message);
    }

    @Test
    public void loadsAllPendingMessages() throws Exception {
        repository.save(createDefaultMessage());
        repository.save(createDefaultMessage());

        assertThat(repository.loadPending()).hasSize(2);
    }

    @Test
    public void cancelledMessagesShouldNotBeIncludedAsPending() throws Exception {
        Message msg = createDefaultMessage();
        msg.cancel();
        repository.save(msg);

        assertThat(repository.loadPending()).isEmpty();
    }

    @Test
    public void sentMessagesShouldNotBeIncludedAsPending() throws Exception {
        Message msg = createDefaultMessage();
        msg.send(new CurrentTimeProvider());
        repository.save(msg);

        assertThat(repository.loadPending()).isEmpty();
    }

    @NonNull
    private Message createDefaultMessage() {
        return new Message(MESSAGE_CONTENT, SUBMISSION_DATE);
    }

    private class CurrentTimeProvider implements TimeProvider {
        @Override
        public Date getCurrentTime() {
            return new Date();
        }
    }
}
