package dev.glimpse.messenger.message.application;

import dev.glimpse.messenger.AbstractIntegrationTest;
import dev.glimpse.messenger.message.entity.MessageContent;
import dev.glimpse.messenger.message.presentation.dto.MessageDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SendingMessageUseCaseIT extends AbstractIntegrationTest {

    @Autowired
    private SendingMessageUseCase sendingMessageUseCase;

    @DisplayName("Check that message is received by websocket")
    @Test
    void testMessageReceived() throws ExecutionException, InterruptedException {
        // Arrange
        StompClientWrapper<MessageDto> stompClientWrapper = new StompClientWrapper<>();
        var senderId = UUID.randomUUID();
        var recipientId = UUID.randomUUID();
        var completableFuture = stompClientWrapper.listen(String.format("/users/%s/messages", recipientId), MessageDto.class);

        // Act
        sendingMessageUseCase.execute(senderId, recipientId, MessageContent.of("Hello"));

        // Assert
        MessageDto messageDto = completableFuture.get();
        assertEquals("Hello", messageDto.getContent());
    }

}