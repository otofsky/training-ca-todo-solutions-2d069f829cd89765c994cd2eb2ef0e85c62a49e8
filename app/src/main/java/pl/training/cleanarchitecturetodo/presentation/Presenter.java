package pl.training.cleanarchitecturetodo.presentation;

public interface Presenter<UI> {
    void attach(UI ui);

    void detach();
}
