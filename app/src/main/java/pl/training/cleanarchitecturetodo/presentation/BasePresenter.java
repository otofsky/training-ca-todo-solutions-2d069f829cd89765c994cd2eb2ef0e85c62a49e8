package pl.training.cleanarchitecturetodo.presentation;

import java.util.LinkedList;
import java.util.Queue;

public abstract class BasePresenter<UI> implements Presenter<UI> {

    private Queue<UICommand<UI>> commandQueue = new LinkedList<>();
    private UI ui;

    @Override
    public void attach(UI ui) {
        this.ui = ui;

        UICommand<UI> cmd = commandQueue.poll();
        while (cmd != null) {
            cmd.execute(this.ui);
            cmd = commandQueue.poll();
        }
    }

    @Override
    public void detach() {
        this.ui = null;
    }

    public void execute(UICommand<UI> cmd) {
        if (this.ui != null) cmd.execute(this.ui);
        else commandQueue.offer(cmd);
    }

    public interface UICommand<UI> {
        void execute(UI ui);
    }
}
