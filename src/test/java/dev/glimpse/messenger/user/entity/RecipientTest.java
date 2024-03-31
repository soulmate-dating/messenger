package dev.glimpse.messenger.user.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RecipientTest {

    @DisplayName("Check that the recipient is created with the correct inputs.")
    @Test
    public void testRecipientCreation() {
        // Arrange
        var id = UUID.randomUUID();

        // Act
        UserId recipient = UserId.of(id);

        // Assert
        assertEquals(id, recipient.getId());
    }

    @DisplayName("Check that the recipient is not created with null.")
    @Test
    public void testRecipientCreationWithNull() {
        // Arrange
        UUID id = null;

        // Act and Assert
        assertThrows(NullPointerException.class, () -> UserId.of(id));
    }

}