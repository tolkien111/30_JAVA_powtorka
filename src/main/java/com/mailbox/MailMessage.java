package com.mailbox;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class MailMessage {
  private final long uid;
  private final LocalDateTime sentTime;
  private final String sender;
  private final String title;
  private final String body;

  public MailMessage(long uid, LocalDateTime sentTime, String sender, String title, String body) {
    this.uid = uid;
    this.sentTime = sentTime;
    this.sender = sender;
    this.title = title;
    this.body = body;
  }

  public long getUid() {
    return uid;
  }

  public LocalDateTime getSentTime() {
    return sentTime;
  }

  public String getSender() {
    return sender;
  }

  public String getTitle() {
    return title;
  }

  public String getBody() {
    return body;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MailMessage that = (MailMessage) o;
    return uid == that.uid && sentTime.equals(that.sentTime) && sender.equals(that.sender) && title.equals(that.title) && body.equals(that.body);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uid, sentTime, sender, title, body);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", MailMessage.class.getSimpleName() + "[", "]")
        .add("sentTime=" + sentTime)
        .add("sender='" + sender + "'")
        .add("title='" + title + "'")
        .toString();
  }
}
