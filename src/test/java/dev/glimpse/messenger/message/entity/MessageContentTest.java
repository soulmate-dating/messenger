package dev.glimpse.messenger.message.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MessageContentTest {

    @DisplayName("Check that the message content is created with the correct inputs.")
    @Test
    public void testMessageContentCreation() {
        // Arrange
        var content = "Hello, World!";

        // Act
        var messageContent = MessageContent.of(content);

        // Assert
        assertEquals(content, messageContent.getValue());
    }

    @DisplayName("Check that the message content is not created with null content.")
    @Test
    public void testMessageContentCreationWithNullContent() {
        // Arrange
        String content = null;

        // Act and Assert
        assertThrows(NullPointerException.class, () -> MessageContent.of(content));
    }

    @DisplayName("Check that the message content is not created with empty content.")
    @Test
    public void testMessageContentCreationWithEmptyContent() {
        // Arrange
        var content = "";

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> MessageContent.of(content));
    }

    @DisplayName("Check that the message content is not created with blank content.")
    @Test
    public void testMessageContentCreationWithBlankContent() {
        // Arrange
        var content = " ";

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> MessageContent.of(content));
    }

    @DisplayName("Check that the message content is not created with content exceeding 1000 characters.")
    @Test
    public void testMessageContentCreationWithContentExceeding1000Characters() {
        // Arrange
        var content = "a".repeat(1001);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> MessageContent.of(content));
    }


}