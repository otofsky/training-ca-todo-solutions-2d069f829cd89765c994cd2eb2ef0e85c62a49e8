package pl.training.cleanarchitecturetodo.presentation;

public interface AsyncExecutor {

    <T> void submit(Task<T> task, ResultCallback<T> callback);

    interface Task<Result> {
        Result call();
    }

    interface ResultCallback<Result> {
        void onComplete(Result result);

        void onError(Exception ex);
    }
}
