package dev.glimpse.messenger.message.adapter.converter;

import dev.glimpse.messenger.message.entity.MessageTag;
import dev.glimpse.messenger.message.presentation.dto.MessageTagDto;
import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class MessageTagToMessageTagDtoConverter implements Converter<MessageTag, MessageTagDto> {
    @Override
    public MessageTagDto convert(@NonNull MessageTag source) {
        if (Objects.isNull(source.getName()) || Objects.isNull(source.getExternalId())) {
            return null;
        }
        return new MessageTagDto(
                MessageTagDto.NameEnum.valueOf(source.getName().name()),
                source.getExternalId()
        );
    }
}
