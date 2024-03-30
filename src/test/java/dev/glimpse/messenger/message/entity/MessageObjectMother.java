package dev.glimpse.messenger.message.entity;

import dev.glimpse.messenger.user.entity.Recipient;
import dev.glimpse.messenger.user.entity.Sender;

import java.util.UUID;

public class MessageObjectMother {

    public static Message createMessage() {
        return createMessage(Sender.of(UUID.randomUUID()), Recipient.of(UUID.randomUUID()));
    }

    public static Message createMessage(Sender sender, Recipient recipient) {
        return Message.builder()
                .sender(sender)
                .recipient(recipient)
                .content(MessageContent.of("SOME CONTENT"))
                .build();
    }

    public static Message createMessage(MessageTag tag) {
        return Message.builder()
                .tag(tag)
                .content(MessageContent.of("SOME CONTENT"))
                .sender(Sender.of(UUID.randomUUID()))
                .recipient(Recipient.of(UUID.randomUUID()))
                .build();
    }

}

