package dev.glimpse.messenger.message.presentation.dto;

import java.util.UUID;

public class SendMessageDtoObjectMother {

    public static SendMessageDto create() {
        return new SendMessageDto(
                UUID.randomUUID(),
                "content"
        );
    }

    public static SendMessageDto create(MessageTagDto tagDto) {
        return create().tag(tagDto);
    }

}