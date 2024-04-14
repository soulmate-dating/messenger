package dev.glimpse.messenger.user.entity;

import dev.glimpse.messenger.message.entity.Message;
import dev.glimpse.messenger.message.entity.MessageObjectMother;

import java.util.Set;

public class UserObjectMother {

    public static UserWithCompanionAndMessage createUserWithCompanion() {
        UserId companionId = UserId.of();
        User companion = createUser(companionId);
        User user = User.builder()
                .id(UserId.of())
                .companions(Set.of(companionId.getId()))
                .build();
        Message message = MessageObjectMother.createMessage(user.getId(), companionId);
        return new UserWithCompanionAndMessage(user, companion, message);
    }

    public static User createUser(UserId userId) {
        return User.builder()
                .id(userId)
                .build();
    }

    public record UserWithCompanionAndMessage(
            User user,
            User companion,
            Message message
    ) {

    }

}
