package dev.glimpse.messenger.message.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MessageSentDateTest {

    @DisplayName("Check that the message sent date is created with the correct inputs.")
    @Test
    public void testMessageSentDateCreation() {
        // Arrange
        var sentTime = Instant.now();

        // Act
        var messageSentDate = MessageSentDate.of(sentTime);

        // Assert
        assertEquals(sentTime, messageSentDate.getValue());
    }

    @DisplayName("Check that the message sent date is not created with null.")
    @Test
    public void testMessageSentDateCreationWithNull() {
        // Arrange
        Instant sentTime = null;

        // Act and Assert
        assertThrows(NullPointerException.class, () -> MessageSentDate.of(sentTime));
    }

}