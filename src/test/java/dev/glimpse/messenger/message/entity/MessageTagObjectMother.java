package dev.glimpse.messenger.message.entity;

import java.util.UUID;

public class MessageTagObjectMother {

    public static MessageTag create() {
        return MessageTag.of(MessageTagName.REACTION, UUID.randomUUID());
    }

}
