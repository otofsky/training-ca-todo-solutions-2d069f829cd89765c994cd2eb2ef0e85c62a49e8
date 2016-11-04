package pl.training.cleanarchitecturetodo.persistance;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import pl.training.cleanarchitecturetodo.domain.MessageRepository;

@Module
public abstract class PersistanceModule {

    @Binds
    @Singleton
    public abstract MessageRepository messageRepository(SQLiteMessageRepository r);
}
