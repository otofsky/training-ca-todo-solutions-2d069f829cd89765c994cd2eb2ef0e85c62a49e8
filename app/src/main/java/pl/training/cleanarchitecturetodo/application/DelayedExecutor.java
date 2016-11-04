package pl.training.cleanarchitecturetodo.application;

public interface DelayedExecutor {
    void executeDelayed(Runnable runnable, long delay);
}
