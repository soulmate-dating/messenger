package dev.glimpse.messenger.message.application.event;

import dev.glimpse.messenger.message.entity.Message;

public interface AfterSaveMessageEventHandler {

    void handleAfterSave(Message message);

}
