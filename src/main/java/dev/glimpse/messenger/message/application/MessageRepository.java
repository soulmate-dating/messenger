package dev.glimpse.messenger.message.application;

import dev.glimpse.messenger.common.Paging;
import dev.glimpse.messenger.message.entity.Message;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface MessageRepository {

    Message save(Message message);

    Page<Message> findMessagesInDialog(UUID userId, UUID companionId, Paging paging);

}
