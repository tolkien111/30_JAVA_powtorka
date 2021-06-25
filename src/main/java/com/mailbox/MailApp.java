package com.mailbox;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

public class MailApp {
  public static void main(String[] args) {
    MailBox mailBox = new MailBox();

    // Run mailbox monitoring in a separate thread
    newSingleThreadScheduledExecutor()
        .scheduleAtFixedRate(mailBox::pullMessages, 0, 1, TimeUnit.SECONDS);
  }
}
