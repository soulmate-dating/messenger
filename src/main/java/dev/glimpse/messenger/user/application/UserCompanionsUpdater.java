package dev.glimpse.messenger.user.application;

import dev.glimpse.messenger.message.application.event.AfterSaveMessageEventHandler;
import dev.glimpse.messenger.message.entity.Message;
import dev.glimpse.messenger.user.entity.User;
import dev.glimpse.messenger.user.entity.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCompanionsUpdater implements AfterSaveMessageEventHandler {

    private final UserRepository userRepository;

    @Async
    @Override
    public void handleAfterSave(Message message) {
        updateCompanionForUser(message.getRecipientId(), message.getSenderId());
        updateCompanionForUser(message.getSenderId(), message.getRecipientId());
    }

    private void updateCompanionForUser(UserId userId, UserId companionId) {
        User user = findUserById(userId);
        user.addCompanion(companionId);
        userRepository.update(user);
    }

    private User findUserById(UserId userId) {
        return userRepository.find(userId)
                .orElseGet(() -> User.builder().id(userId).build());
    }

}
