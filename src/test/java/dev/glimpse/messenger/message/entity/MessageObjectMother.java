package dev.glimpse.messenger.message.entity;

import dev.glimpse.messenger.user.entity.Recipient;
import dev.glimpse.messenger.user.entity.Sender;

import java.util.UUID;

public class MessageObjectMother {

     public static Message createMessage() {
        return Message.of(
                Sender.of(UUID.randomUUID()),
                Recipient.of(UUID.randomUUID()),
                MessageContent.of("SOME CONTENT")
        );
    }

}
