package pl.training.cleanarchitecturetodo.domain;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pl.training.cleanarchitecturetodo.domain.validators.MessageLengthValidator;
import pl.training.cleanarchitecturetodo.domain.validators.MultipleValidators;
import pl.training.cleanarchitecturetodo.domain.validators.NotEmptyMessageValidator;

@Module
public class DomainModule {

    @Provides
    @Singleton
    public static MessageValidator provideMessageValidator() {
        return new MultipleValidators(new NotEmptyMessageValidator(), new MessageLengthValidator());
    }
}
