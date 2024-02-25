package dev.glimpse.messenger.message.application;

import dev.glimpse.messenger.common.Paging;
import dev.glimpse.messenger.message.entity.Message;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class FindingMessagesUseCase {

    private final MessageRepository messageRepository;

    public Page<Message> execute(@NonNull UUID userId, @NonNull UUID companionId, @NonNull Paging paging) {
        return messageRepository.findMessagesInDialog(userId, companionId, paging);
    }

}
