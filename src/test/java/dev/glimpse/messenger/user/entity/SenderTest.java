package dev.glimpse.messenger.user.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SenderTest {

    @DisplayName("Check that the sender is created with the correct inputs.")
    @Test
    public void testSenderCreation() {
        // Arrange
        var id = UUID.randomUUID();

        // Act
        var sender = Sender.of(id);

        // Assert
        assertEquals(id, sender.getId());
    }

    @DisplayName("Check that the sender is not created with null.")
    @Test
    public void testSenderCreationWithNull() {
        // Arrange
        UUID id = null;

        // Act and Assert
        assertThrows(NullPointerException.class, () -> Sender.of(id));
    }

}