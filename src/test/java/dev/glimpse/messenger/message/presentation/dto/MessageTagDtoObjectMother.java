package dev.glimpse.messenger.message.presentation.dto;

import java.util.UUID;

public class MessageTagDtoObjectMother {

    public static MessageTagDto create() {
        return new MessageTagDto(
                MessageTagDto.NameEnum.REACTION,
                UUID.randomUUID()
        );
    }

}
