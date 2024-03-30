package dev.glimpse.messenger.message.adapter.converter;

import dev.glimpse.messenger.message.entity.MessageTag;
import dev.glimpse.messenger.message.entity.MessageTagName;
import dev.glimpse.messenger.message.presentation.dto.MessageTagDto;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Objects;

@RequiredArgsConstructor
@Component
public class MessageTagDtoToMessageTagConverter implements Converter<MessageTagDto, MessageTag> {

    private final Converter<MessageTagDto.NameEnum, MessageTagName> messageTagNameConverter;

    @Override
    public MessageTag convert(@Nullable MessageTagDto source) {
        if (source == null) {
            return null;
        }
        MessageTagName tagName = messageTagNameConverter.convert(source.getName());
        Objects.requireNonNull(tagName);
        return MessageTag.of(tagName, source.getExternalId());
    }
}
