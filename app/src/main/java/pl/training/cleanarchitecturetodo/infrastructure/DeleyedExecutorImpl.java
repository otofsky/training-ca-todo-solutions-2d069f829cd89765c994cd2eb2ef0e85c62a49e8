package pl.training.cleanarchitecturetodo.infrastructure;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import pl.training.cleanarchitecturetodo.application.DelayedExecutor;

public class DeleyedExecutorImpl implements DelayedExecutor {
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    @Inject
    public DeleyedExecutorImpl() {
    }

    @Override
    public void executeDelayed(Runnable runnable, long delay) {
        executorService.schedule(runnable, delay, TimeUnit.MILLISECONDS);
    }
}
