package dev.glimpse.messenger.message.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.glimpse.messenger.AbstractIntegrationTest;
import dev.glimpse.messenger.message.presentation.dto.SendMessageDtoObjectMother;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MessageApiImplIT extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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

}