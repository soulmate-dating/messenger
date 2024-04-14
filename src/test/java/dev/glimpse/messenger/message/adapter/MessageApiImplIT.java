package dev.glimpse.messenger.message.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.glimpse.messenger.AbstractIntegrationTest;
import dev.glimpse.messenger.message.entity.Message;
import dev.glimpse.messenger.message.entity.MessageObjectMother;
import dev.glimpse.messenger.message.entity.MessageTag;
import dev.glimpse.messenger.message.entity.MessageTagObjectMother;
import dev.glimpse.messenger.message.infrastructure.CassandraMessageRepository;
import dev.glimpse.messenger.message.presentation.dto.MessageTagDto;
import dev.glimpse.messenger.message.presentation.dto.MessageTagDtoObjectMother;
import dev.glimpse.messenger.message.presentation.dto.SendMessageDtoObjectMother;
import dev.glimpse.messenger.user.entity.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MessageApiImplIT extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CassandraMessageRepository messageRepository;

    @DisplayName("Check that the message is sent.")
    @Test
    public void testMessageIsSent() throws Exception {
        // Arrange
        UUID senderId = UUID.randomUUID();
        var sendMessageDto = SendMessageDtoObjectMother.create();

        // Act
        ResultActions perform = mockMvc.perform(post(String.format("/users/%s/messages", senderId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sendMessageDto)));

        // Assert
        perform.andExpect(status().isOk())
               .andExpect(jsonPath("$.id").isNotEmpty())
               .andExpect(jsonPath("$.sender_id").value(senderId.toString()))
               .andExpect(jsonPath("$.recipient_id").value(sendMessageDto.getRecipientId().toString()))
               .andExpect(jsonPath("$.content").value(sendMessageDto.getContent()));
    }

    @DisplayName("Check that the message is not sent when id is not valid.")
    @Test
    public void testMessageIsNotSentWhenIdIsNotValid() throws Exception {
        // Arrange
        var sendMessageDto = SendMessageDtoObjectMother.create();

        // Act
        ResultActions perform = mockMvc.perform(post(String.format("/users/%s/messages", "invalid"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sendMessageDto)));

        // Assert
        perform.andExpect(status().isBadRequest());
    }

    @DisplayName("Check that the finding messages returns empty page of messages when nothing found.")
    @Test
    public void testFindingMessagesReturnsPageOfMessages() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();
        UUID companionId = UUID.randomUUID();

        // Act
        ResultActions perform = mockMvc.perform(get(String.format("/users/%s/messages", userId))
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("companion_id", companionId.toString())
                .queryParam("page", "0")
                .queryParam("size", "10")
        );

        // Assert
        perform.andExpect(status().isOk())
               .andExpect(jsonPath("$.page").value(0))
               .andExpect(jsonPath("$.size").value(10))
               .andExpect(jsonPath("$.total").value(0))
               .andExpect(jsonPath("$.messages").isArray());
    }

    @DisplayName("Check that the finding messages returns page with existing message.")
    @Test
    public void testFindingMessagesReturnsPageWithExistingMessage() throws Exception {
        // Arrange
        Message message = MessageObjectMother.createMessage();
        messageRepository.save(message);

        // Act
        ResultActions perform = mockMvc.perform(get(String.format("/users/%s/messages", message.getRecipientId().getId().toString()))
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("companion_id", message.getSenderId().getId().toString())
                .queryParam("page", "0")
                .queryParam("size", "10")
        );

        // Assert
        perform.andExpect(status().isOk())
               .andExpect(jsonPath("$.page").value(0))
               .andExpect(jsonPath("$.size").value(10))
               .andExpect(jsonPath("$.total").value(1))
               .andExpect(jsonPath("$.messages").isArray())
               .andExpect(jsonPath("$.messages[0].id").isNotEmpty())
               .andExpect(jsonPath("$.messages[0].sender_id").isNotEmpty())
               .andExpect(jsonPath("$.messages[0].recipient_id").isNotEmpty())
               .andExpect(jsonPath("$.messages[0].content").isNotEmpty());
    }

    @DisplayName("Check that the finding messages returns messages starting from concrete message.")
    @Test
    void testFindingMessagesReturnsMessagesStartingFromConcreteMessage() throws Exception {
        // Arrange
        UserId senderId = UserId.of(UUID.randomUUID());
        UserId recipient = UserId.of(UUID.randomUUID());
        List<Message> messages = List.of(
                MessageObjectMother.createMessage(senderId, recipient),
                MessageObjectMother.createMessage(senderId, recipient),
                MessageObjectMother.createMessage(senderId, recipient),
                MessageObjectMother.createMessage(senderId, recipient),
                MessageObjectMother.createMessage(senderId, recipient)
        );
        messageRepository.saveAll(messages);
        messages = messageRepository.findAll();
        Message fromMessage = messages.get(2);

        List<Message> filteredMessages = messages.subList(3, messages.size());

        // Act
        ResultActions perform = mockMvc.perform(get(String.format("/users/%s/messages", recipient.getId().toString()))
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("companion_id", senderId.getId().toString())
                .queryParam("from_message_id", fromMessage.getId().getValue().toString())
                .queryParam("page", "0")
                .queryParam("size", "10")
        );

        // Assert
        ResultActions resultActions = perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.total").value(filteredMessages.size()))
                .andExpect(jsonPath("$.messages").isArray());

        for (int i = 0; i < filteredMessages.size(); i++) {
            Message message = filteredMessages.get(i);
            resultActions.andExpect(jsonPath(String.format("$.messages[%d].id", i)).value(message.getId().getValue().toString()))
                    .andExpect(jsonPath(String.format("$.messages[%d].sender_id", i)).value(message.getSenderId().getId().toString()))
                    .andExpect(jsonPath(String.format("$.messages[%d].recipient_id", i)).value(message.getRecipientId().getId().toString()))
                    .andExpect(jsonPath(String.format("$.messages[%d].content", i)).value(message.getContent().getValue()));
        }
    }

    @DisplayName("Check that sending message with tag is successful.")
    @Test
    public void testSendingMessageWithTag() throws Exception {
        // Arrange
        UUID senderId = UUID.randomUUID();
        MessageTagDto tagDto = MessageTagDtoObjectMother.create();
        var sendMessageDto = SendMessageDtoObjectMother.create(tagDto);

        // Act
        ResultActions perform = mockMvc.perform(post(String.format("/users/%s/messages", senderId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sendMessageDto)));

        // Assert
        perform.andExpect(status().isOk())
               .andExpect(jsonPath("$.id").isNotEmpty())
               .andExpect(jsonPath("$.sender_id").value(senderId.toString()))
               .andExpect(jsonPath("$.recipient_id").value(sendMessageDto.getRecipientId().toString()))
               .andExpect(jsonPath("$.content").value(sendMessageDto.getContent()))
               .andExpect(jsonPath("$.tag.name").value(tagDto.getName().toString()))
               .andExpect(jsonPath("$.tag.external_id").value(tagDto.getExternalId().toString()));
    }

    @DisplayName("Check that getting messages with tag is successful.")
    @Test
    public void testGettingMessagesWithTag() throws Exception {
        // Arrange
        MessageTag tag = MessageTagObjectMother.create();
        Message message = MessageObjectMother.createMessage(tag);
        messageRepository.save(message);

        // Act
        ResultActions perform = mockMvc.perform(get(String.format("/users/%s/messages", message.getRecipientId().getId().toString()))
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("companion_id", message.getSenderId().getId().toString())
                .queryParam("page", "0")
                .queryParam("size", "10")
        );

        // Assert
        assertNotNull(message.getTag());
        assertNotNull(message.getTag().getName());
        assertNotNull(message.getTag().getExternalId());

        perform.andExpect(status().isOk())
               .andExpect(jsonPath("$.page").value(0))
               .andExpect(jsonPath("$.size").value(10))
               .andExpect(jsonPath("$.total").value(1))
               .andExpect(jsonPath("$.messages").isArray())
               .andExpect(jsonPath("$.messages[0].id").isNotEmpty())
                .andExpect(jsonPath("$.messages[0].sender_id").value(message.getSenderId().getId().toString()))
                .andExpect(jsonPath("$.messages[0].recipient_id").value(message.getRecipientId().getId().toString()))
               .andExpect(jsonPath("$.messages[0].content").value(message.getContent().getValue()))
               .andExpect(jsonPath("$.messages[0].tag.name").value(message.getTag().getName().toString()))
               .andExpect(jsonPath("$.messages[0].tag.external_id").value(message.getTag().getExternalId().toString()));
    }

}