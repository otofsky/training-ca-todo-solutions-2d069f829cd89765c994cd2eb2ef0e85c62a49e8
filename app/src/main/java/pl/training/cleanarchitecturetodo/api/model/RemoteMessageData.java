package pl.training.cleanarchitecturetodo.api.model;

import java.util.Date;

public class RemoteMessageData {
    private final long id;
    private final String content;
    private final Date publicationDate;

    private RemoteMessageData() {
        this.id = 0;
        this.content = null;
        this.publicationDate = null;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    @Override
    public String toString() {
        return getContent();
    }
}
