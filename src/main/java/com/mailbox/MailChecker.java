package com.mailbox;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.util.MimeMessageParser;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

import static javax.mail.Session.getDefaultInstance;

public class MailChecker {
  private ScheduledFuture<?> handle = null;

  public List<MailMessage> getMessages() {
    try {
      Properties properties = loadProperties();
      Session mailSession = getDefaultInstance(loadProperties());

      Store store = mailSession.getStore("imaps");
      store.connect(properties.getProperty("user"), properties.getProperty("password"));

      Folder folder = store.getFolder("inbox");
      folder.open(Folder.READ_ONLY);

      List<MailMessage> messages = Arrays.stream(folder.getMessages())
          .map(message -> map((UIDFolder) folder, message))
          .collect(Collectors.toList());

      folder.close(false);
      store.close();

      return messages;
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static Properties loadProperties() {
    try {
      URL propertiesUrl = Thread.currentThread().getContextClassLoader().getResource("mail.properties");
      if (propertiesUrl == null) {
        throw new RuntimeException(String.format("Properties [%s] could not be found.", "mail.properties"));
      }

      String propertiesPath = propertiesUrl.getPath();

      Properties properties = new Properties();
      properties.load(new FileInputStream(propertiesPath));

      return properties;
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }
  }

  private static MailMessage map(UIDFolder uidFolder, Message message) {
    try {
      long messageId = uidFolder.getUID(message);
      LocalDateTime sentTime = toLocalDateTime(message.getSentDate());
      String sender = message.getFrom()[0].toString();
      String subject = message.getSubject();
      String body = StringUtils.abbreviate(new MimeMessageParser((MimeMessage) message).parse().getPlainContent(), 20);

      return new MailMessage(messageId, sentTime, sender, subject, body);
    } catch (Exception exception) {
      throw new RuntimeException(exception);
    }
  }

  private static LocalDateTime toLocalDateTime(Date date) {
    ZoneId zoneId = ZoneId.systemDefault();
    Instant instant = date.toInstant();
    ZonedDateTime zonedDateTime = instant.atZone(zoneId);
    return zonedDateTime.toLocalDateTime();
  }
}
