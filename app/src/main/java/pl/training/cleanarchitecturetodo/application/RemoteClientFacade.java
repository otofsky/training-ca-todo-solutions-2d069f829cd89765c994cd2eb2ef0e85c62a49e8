package pl.training.cleanarchitecturetodo.application;

import java.util.Date;
import java.util.List;

import pl.training.cleanarchitecturetodo.domain.Message;

public interface RemoteClientFacade {
    void send(Message message);

    List<RemoteMessage> loadMessages();

    class RemoteMessage {
        private final String content;
        private final Date publishDate;

        public RemoteMessage(String content, Date publishDate) {
            this.content = content;
            this.publishDate = publishDate;
        }

        public String getContent() {
            return content;
        }

        public Date getPublishDate() {
            return publishDate;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RemoteMessage that = (RemoteMessage) o;

            if (content != null ? !content.equals(that.content) : that.content != null)
                return false;
            return publishDate != null ? publishDate.equals(that.publishDate) : that.publishDate == null;

        }

        @Override
        public int hashCode() {
            int result = content != null ? content.hashCode() : 0;
            result = 31 * result + (publishDate != null ? publishDate.hashCode() : 0);
            return result;
        }
    }
}
