package dev.glimpse.messenger.message.adapter.converter;

import dev.glimpse.messenger.message.entity.MessageObjectMother;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MessageToMessageDtoConverterTest {

    @DisplayName("Check that the message is converted to message dto.")
    @Test
    public void testMessageToMessageDtoConversion() {
        // Arrange
        var message = MessageObjectMother.createMessage();
        var sut = new MessageToMessageDtoConverter(new MessageTagToMessageTagDtoConverter());

        // Act
        var messageDto = sut.convert(message);

        // Assert
        assertNotNull(messageDto);
        assertEquals(message.getId().getValue(), messageDto.getId());
        assertEquals(message.getSender().getId(), messageDto.getSenderId());
        assertEquals(message.getRecipient().getId(), messageDto.getRecipientId());
        assertEquals(message.getSentAt().getValue().atOffset(ZoneOffset.UTC), messageDto.getDate());
        assertEquals(message.getContent().getValue(), messageDto.getContent());
    }

}