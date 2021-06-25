package com.mailbox;

import java.util.List;

public interface Observer {
  void update(List<MailMessage> mailMessage);
}
