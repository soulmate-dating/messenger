package dev.glimpse.messenger.message.presentation.dto;

import java.util.UUID;

public class SendMessageDtoObjectMother {

    public static SendMessageDto create() {
        return new SendMessageDto()
                .recipientId(UUID.randomUUID())
                .content("content");
    }

}