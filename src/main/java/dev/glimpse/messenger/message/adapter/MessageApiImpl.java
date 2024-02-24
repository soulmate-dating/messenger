package dev.glimpse.messenger.message.adapter;

import dev.glimpse.messenger.message.application.SendingMessageUseCase;
import dev.glimpse.messenger.message.entity.Message;
import dev.glimpse.messenger.message.entity.MessageContent;
import dev.glimpse.messenger.message.presentation.api.MessageApi;
import dev.glimpse.messenger.message.presentation.dto.MessageDto;
import dev.glimpse.messenger.message.presentation.dto.SendMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class MessageApiImpl implements MessageApi {

    private final ConversionService conversionService;
    private final SendingMessageUseCase sendingMessageUseCase;

    @Override
    public ResponseEntity<MessageDto> sendMessage(UUID id, SendMessageDto sendMessageDto) {
        MessageContent messageContent = MessageContent.of(sendMessageDto.getContent());

        Message sentMessage = sendingMessageUseCase.execute(id, sendMessageDto.getRecipientId(), messageContent);
        MessageDto converted = conversionService.convert(sentMessage, MessageDto.class);
        return ResponseEntity.ok(converted);
    }
}
