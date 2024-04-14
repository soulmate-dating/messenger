package dev.glimpse.messenger.user.adapter;

import dev.glimpse.messenger.AbstractIntegrationTest;
import dev.glimpse.messenger.message.application.MessageRepository;
import dev.glimpse.messenger.user.application.FindingUserProfilesUseCase;
import dev.glimpse.messenger.user.application.UserRepository;
import dev.glimpse.messenger.user.entity.UserObjectMother;
import dev.glimpse.messenger.user.entity.UserProfile;
import dev.glimpse.messenger.user.entity.UserProfileObjectMother;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CompanionApiImplIT extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FindingUserProfilesUseCase findingUserProfilesUseCaseMock;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @DisplayName("Check that getting companions returns existing companions")
    @Test
    void testGetCompanions() throws Exception {
        // Arrange
        var userWithCompanion = UserObjectMother.createUserWithCompanion();
        var user = userWithCompanion.user();
        var companion = userWithCompanion.companion();
        var message = userWithCompanion.message();
        userRepository.save(user);
        userRepository.save(companion);
        messageRepository.save(message);

        UserProfile userProfile = UserProfileObjectMother.create();
        when(findingUserProfilesUseCaseMock.execute(any())).thenReturn(Map.of(companion.getId(), userProfile));

        // Act
        ResultActions perform = mockMvc.perform(get("/users/{id}/companions", user.getId().getId()));

        // Assert
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].companion.id").value(user.getCompanions().iterator().next().getId().toString()))
                .andExpect(jsonPath("$[0].companion").exists())
                .andExpect(jsonPath("$[0].companion.first_name").value(userProfile.firstName()))
                .andExpect(jsonPath("$[0].companion.last_name").value(userProfile.lastName()))
                .andExpect(jsonPath("$[0].companion.pic_link").value(userProfile.avatarLink().toString()))
                .andExpect(jsonPath("$[0].last_message").value(message.getContent().getValue()))
                .andExpect(jsonPath("$[0].is_your_turn").value(false));
    }

}