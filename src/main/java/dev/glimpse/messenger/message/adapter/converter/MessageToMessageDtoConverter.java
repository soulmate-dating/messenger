package dev.glimpse.messenger.message.adapter.converter;

import dev.glimpse.messenger.message.entity.Message;
import dev.glimpse.messenger.message.entity.MessageTag;
import dev.glimpse.messenger.message.presentation.dto.MessageDto;
import dev.glimpse.messenger.message.presentation.dto.MessageTagDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;

@Component
@RequiredArgsConstructor
public class MessageToMessageDtoConverter implements Converter<Message, MessageDto> {

    private final Converter<MessageTag, MessageTagDto> messageTagConverter;

    @Override
    public MessageDto convert(@NonNull Message source) {
        return new MessageDto()
                .id(source.getId().getValue())
                .senderId(source.getSenderId().getId())
                .recipientId(source.getRecipientId().getId())
                .date(source.getSentAt().getValue().atOffset(ZoneOffset.UTC))
                .content(source.getContent().getValue())
                .tag(source.getMessageTag().map(messageTagConverter::convert).orElse(null));
    }
}
