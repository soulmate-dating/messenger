package dev.glimpse.messenger.message.adapter.converter;

import dev.glimpse.messenger.message.entity.Message;
import dev.glimpse.messenger.message.presentation.dto.MessageDto;
import dev.glimpse.messenger.message.presentation.dto.MessagePageDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class PageMessageToMessagePageDtoConverter implements Converter<Page<Message>, MessagePageDto> {

    private final Converter<Message, MessageDto> messageMessageDtoConverter;

    @Override
    public MessagePageDto convert(@NonNull Page<Message> source) {
        return new MessagePageDto()
                .page(source.getNumber())
                .size(source.getSize())
                .total((int) source.getTotalElements())
                .messages(convertMessages(source.getContent()));
    }

    private List<MessageDto> convertMessages(List<Message> messages) {
        return messages.stream()
                .map(messageMessageDtoConverter::convert)
                .toList();
    }

}
