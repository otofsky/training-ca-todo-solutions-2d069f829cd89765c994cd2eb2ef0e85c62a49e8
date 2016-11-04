package pl.training.cleanarchitecturetodo.infrastructure;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import pl.training.cleanarchitecturetodo.application.DelayedExecutor;
import pl.training.cleanarchitecturetodo.domain.TimeProvider;
import pl.training.cleanarchitecturetodo.presentation.AsyncExecutor;

@Module
public abstract class InfrastructureModule {

    @Binds
    @Singleton
    public abstract TimeProvider timeProvider(AndroidTimeProvider p);

    @Binds
    @Singleton
    public abstract DelayedExecutor delayedExecutor(DeleyedExecutorImpl e);

    @Binds
    @Singleton
    public abstract AsyncExecutor asyncExecutor(AndroidAsyncExecutor e);
}
