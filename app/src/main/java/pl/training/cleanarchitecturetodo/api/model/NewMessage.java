package pl.training.cleanarchitecturetodo.api.model;

public class NewMessage {
    private final String content;

    public NewMessage(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
