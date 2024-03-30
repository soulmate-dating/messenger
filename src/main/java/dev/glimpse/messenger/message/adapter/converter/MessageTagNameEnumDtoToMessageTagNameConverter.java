package dev.glimpse.messenger.message.adapter.converter;

import dev.glimpse.messenger.message.entity.MessageTagName;
import dev.glimpse.messenger.message.presentation.dto.MessageTagDto;
import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MessageTagNameEnumDtoToMessageTagNameConverter implements Converter<MessageTagDto.NameEnum, MessageTagName> {
    @Override
    public MessageTagName convert(@NonNull MessageTagDto.NameEnum source) {
        return switch (source) {
            case REPLY -> MessageTagName.REPLY;
            case REACTION -> MessageTagName.REACTION;
        };
    }
}
