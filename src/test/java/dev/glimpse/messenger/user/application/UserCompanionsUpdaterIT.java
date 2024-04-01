package dev.glimpse.messenger.user.application;

import dev.glimpse.messenger.AbstractIntegrationTest;
import dev.glimpse.messenger.message.application.MessageRepository;
import dev.glimpse.messenger.message.entity.Message;
import dev.glimpse.messenger.message.entity.MessageObjectMother;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class UserCompanionsUpdaterIT extends AbstractIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @DisplayName("Check that the companions are updated after a message is saved.")
    @Test
    void testHandleAfterSave() throws InterruptedException {
        // Arrange
        Message message = MessageObjectMother.createMessage();

        // Act
        messageRepository.save(message);

        // Assert
        Thread.sleep(1000); // wait untill the async task is completed
        Set<UUID> companions = userRepository.find(message.getRecipientId()).get().getCompanions();
        assertThat(companions).contains(message.getSenderId().getId());

        companions = userRepository.find(message.getSenderId()).get().getCompanions();
        assertThat(companions).contains(message.getRecipientId().getId());
    }

}