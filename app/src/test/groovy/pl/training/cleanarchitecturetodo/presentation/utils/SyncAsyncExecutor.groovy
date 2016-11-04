package pl.training.cleanarchitecturetodo.presentation.utils

import pl.training.cleanarchitecturetodo.presentation.AsyncExecutor

class SyncAsyncExecutor implements AsyncExecutor {
    @Override
    def <T> void submit(AsyncExecutor.Task<T> task, AsyncExecutor.ResultCallback<T> callback) {
        try {
            def result = task.call();
            callback.onComplete(result)
        } catch (Exception ex) {
            callback.onError(ex);
        }
    }
}
