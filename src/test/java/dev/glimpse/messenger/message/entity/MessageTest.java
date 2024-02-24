package dev.glimpse.messenger.message.entity;

import dev.glimpse.messenger.user.entity.Recipient;
import dev.glimpse.messenger.user.entity.Sender;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.TemporalUnitWithinOffset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageTest {

    @DisplayName("Check that the message is created with the correct inputs.")
    @Test
    public void testMessageCreation() {
        // Arrange
        var sender = Sender.of(UUID.randomUUID());
        var recipient = Recipient.of(UUID.randomUUID());
        var messageContent = MessageContent.of("Hello, World!");

        // Act
        var message = Message.of(sender, recipient, messageContent);

        // Act
        assertEquals(sender, message.getSender());
        assertEquals(recipient, message.getRecipient());
        assertEquals(messageContent, message.getContent());
    }

    @DisplayName("Check that the message is created with now sent date.")
    @Test
    public void testMessageCreationWithNowSentDate() {
        // Arrange
        var sender = Sender.of(UUID.randomUUID());
        var recipient = Recipient.of(UUID.randomUUID());
        var messageContent = MessageContent.of("Hello, World!");
        var now = Instant.now();

        // Act
        var sentAt = Message.of(sender, recipient, messageContent).getSentAt().getValue();

        // Act
        Assertions.assertThat(now).isCloseTo(sentAt, new TemporalUnitWithinOffset(100, ChronoUnit.MILLIS));
    }

}