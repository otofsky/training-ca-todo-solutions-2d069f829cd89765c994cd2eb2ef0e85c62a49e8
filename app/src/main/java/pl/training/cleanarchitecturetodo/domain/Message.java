package pl.training.cleanarchitecturetodo.domain;

import java.util.Date;

public class Message {
    private Long id;
    private final String content;
    private final Date submissionDate;
    private State currentState;
    private Date sendTime;

    public Message(String content, Date submissionDate) {
        this.content = content;
        this.submissionDate = submissionDate;
        this.currentState = State.PENDING;
    }

    public Message(long messageId, String content, Date submissionDate, State currentState, Date sendTime) {
        // deserialization usage only
        this.id = messageId;
        this.content = content;
        this.submissionDate = submissionDate;
        this.currentState = currentState;
        this.sendTime = sendTime;
    }

    public String getContent() {
        return content;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public State getState() {
        return currentState;
    }

    public void cancel() {
        if (this.currentState.isTerminal()) {
            throw new MessageStateChangeException("Unable to cancel canceled message");
        }
        this.currentState = State.CANCELED;
    }

    public void send(TimeProvider timeProvider) {
        if (this.currentState.isTerminal()) {
            throw new MessageStateChangeException("Unable to send already sent message");
        }
        this.currentState = State.SENT;
        this.sendTime = timeProvider.getCurrentTime();
    }

    public Date getSendTime() {
        return this.sendTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isPending() {
        return currentState == State.PENDING;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (!content.equals(message.content)) return false;
        if (!submissionDate.equals(message.submissionDate)) return false;
        if (currentState != message.currentState) return false;
        if (sendTime != null ? !sendTime.equals(message.sendTime) : message.sendTime != null)
            return false;
        return id != null ? id.equals(message.id) : message.id == null;
    }

    @Override
    public int hashCode() {
        int result = content.hashCode();
        result = 31 * result + submissionDate.hashCode();
        result = 31 * result + currentState.hashCode();
        result = 31 * result + (sendTime != null ? sendTime.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }

    public enum State {
        PENDING(false), CANCELED(true), SENT(true);

        private final boolean terminal;

        State(boolean terminal) {
            this.terminal = terminal;
        }

        private boolean isTerminal() {
            return terminal;
        }
    }
}
