package dev.glimpse.messenger.message.infrastructure;

import dev.glimpse.messenger.message.application.MessageSender;
import dev.glimpse.messenger.message.entity.Message;
import dev.glimpse.messenger.message.presentation.dto.MessageDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class WebSocketMessageSender implements MessageSender {

    private static final String MESSAGE_TOPIC_FORMAT = "/messages";

    private final SimpMessagingTemplate messagingTemplate;
    private final ConversionService conversionService;

    @Async
    @Override
    public void send(@NonNull UUID recipientId, @NonNull Message message) {
        MessageDto messageDto = conversionService.convert(message, MessageDto.class);
        Objects.requireNonNull(messageDto);
        messagingTemplate.convertAndSendToUser(recipientId.toString(),
                MESSAGE_TOPIC_FORMAT,
                messageDto);
    }
}
