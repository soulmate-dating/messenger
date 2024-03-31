package dev.glimpse.messenger.message.entity;

import dev.glimpse.messenger.user.entity.UserId;

import java.util.UUID;

public class MessageObjectMother {

    public static Message createMessage() {
        return createMessage(UserId.of(UUID.randomUUID()), UserId.of(UUID.randomUUID()));
    }

    public static Message createMessage(UserId senderId, UserId recipient) {
        return Message.builder()
                .senderId(senderId)
                .recipientId(recipient)
                .content(MessageContent.of("SOME CONTENT"))
                .build();
    }

    public static Message createMessage(MessageTag tag) {
        return Message.builder()
                .tag(tag)
                .content(MessageContent.of("SOME CONTENT"))
                .senderId(UserId.of(UUID.randomUUID()))
                .recipientId(UserId.of(UUID.randomUUID()))
                .build();
    }

}

