package pl.training.cleanarchitecturetodo.application;

import android.support.annotation.NonNull;

import java.util.Date;

public class MessageContent implements Comparable<MessageContent> {
    private final Long localId;
    private final String content;
    private final Date publishDate;
    private final boolean cancelable;

    public MessageContent(Long localId, String content, Date publishDate, boolean cancelable) {
        this.localId = localId;
        this.content = content;
        this.publishDate = publishDate;
        this.cancelable = cancelable;
    }

    public Long getLocalId() {
        return localId;
    }

    public String getContent() {
        return content;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public boolean isCancelable() {
        return cancelable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageContent that = (MessageContent) o;

        if (cancelable != that.cancelable) return false;
        if (localId != null ? !localId.equals(that.localId) : that.localId != null) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        return publishDate != null ? publishDate.equals(that.publishDate) : that.publishDate == null;

    }

    @Override
    public int hashCode() {
        int result = localId != null ? localId.hashCode() : 0;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (publishDate != null ? publishDate.hashCode() : 0);
        result = 31 * result + (cancelable ? 1 : 0);
        return result;
    }

    @Override
    public int compareTo(@NonNull MessageContent other) {
        return this.publishDate.compareTo(other.publishDate);
    }

    @Override
    public String toString() {
        return content;
    }
}
