package dev.glimpse.messenger.message.application;

import dev.glimpse.messenger.message.entity.Message;

public interface MessageRepository {

    Message save(Message message);

}
