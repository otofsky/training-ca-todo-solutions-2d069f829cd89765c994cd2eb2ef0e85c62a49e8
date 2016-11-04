package pl.training.cleanarchitecturetodo.infrastructure;

import android.os.Handler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import pl.training.cleanarchitecturetodo.presentation.AsyncExecutor;

public class AndroidAsyncExecutor implements AsyncExecutor {
    private final ExecutorService executor = Executors.newFixedThreadPool(5);
    private final Handler handler = new Handler();

    @Inject
    public AndroidAsyncExecutor() {
    }

    @Override
    public <T> void submit(final Task<T> task, final ResultCallback<T> callback) {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    deliverResult(task.call(), callback);
                } catch (Exception ex) {
                    deliverError(ex, callback);
                }
            }
        });
    }

    private <T> void deliverError(final Exception ex, final ResultCallback<T> callback) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(ex);
            }
        });
    }

    private <T> void deliverResult(final T result, final ResultCallback<T> callback) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onComplete(result);
            }
        });
    }
}
