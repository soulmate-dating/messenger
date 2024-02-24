package dev.glimpse.messenger.message.adapter.converter;

import dev.glimpse.messenger.message.entity.Message;
import dev.glimpse.messenger.message.presentation.dto.MessageDto;
import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;

@Component
public class MessageToMessageDtoConverter implements Converter<Message, MessageDto> {
    @Override
    public MessageDto convert(@NonNull Message source) {
        return new MessageDto()
                .id(source.getId().getValue())
                .senderId(source.getSender().getId())
                .recipientId(source.getRecipient().getId())
                .date(source.getSentAt().getValue().atOffset(ZoneOffset.UTC))
                .content(source.getContent().getValue());
    }
}
