package com.mailbox;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MailBox implements Subject {

  private final HashSet<Observer> observers = new HashSet<>();

  private final MailChecker mailChecker = new MailChecker();

  private List<MailMessage> messages = new ArrayList<>();

  @Override
  public void registerObserver(Observer observer) {
    observers.add(observer);
  }

  @Override
  public void removeObserver(Observer observer) {
    observers.remove(observer);
  }

  @Override
  public void notifyObservers() {
    observers.forEach(observer -> observer.update(messages));
  }

  public void pullMessages() {
    messages = mailChecker.getMessages();

    notifyObservers();

    System.out.printf("Updated the inbox. Currently there are [%s] messages.%n", messages.size());
  }
}
