package dev.glimpse.messenger.message.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MessageIdTest {

    @DisplayName("Check that the message id is created with the correct inputs.")
    @Test
    public void testMessageIdCreation() {
        // Arrange
        var sentTime = Instant.now();

        // Act
        var messageId = MessageId.ofRandom(MessageSentDate.of(sentTime));

        // Assert
        assertEquals(sentTime, messageId.getSentTime());
    }

    @DisplayName("Check that the message id is not created with null.")
    @Test
    public void testMessageIdCreationWithNull() {
        // Arrange
        MessageSentDate sentTime = null;

        // Act and Assert
        assertThrows(NullPointerException.class, () -> MessageId.ofRandom(sentTime));
    }

}