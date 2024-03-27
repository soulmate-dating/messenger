package dev.glimpse.messenger.message.application;

import dev.glimpse.messenger.message.entity.Message;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface MessageSender {

    void send(@NotNull UUID recipientId, @NotNull Message message);

}
