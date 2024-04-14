package dev.glimpse.messenger.message.infrastructure;

import dev.glimpse.messenger.common.Paging;
import dev.glimpse.messenger.message.application.MessageRepository;
import dev.glimpse.messenger.message.entity.Message;
import dev.glimpse.messenger.message.entity.MessageId;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Primary
@Component
@RequiredArgsConstructor
public class MessageRepositoryImpl implements MessageRepository {

    private final CassandraMessageRepository messageRepository;

    @Override
    public Message save(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public Page<Message> findMessagesInDialog(UUID userId, UUID companionId, UUID fromMessageId, Paging paging) {
        Pageable pageRequest = CassandraPageRequest.of(0, paging.size());
        Slice<Message> page = findDialogMessages(userId, companionId, fromMessageId, pageRequest);
        int pageNumber = 0;
        while (page.hasNext() && pageNumber < paging.page()) {
            page = findDialogMessages(userId, companionId, fromMessageId, page.nextPageable());
            pageNumber++;
        }
        if (pageNumber < paging.page() || page.isEmpty()) {
            return mapToMessagePage(pageNumber, paging.size(), 0, page);
        }
        fromMessageId = setDefaultFromMessageId(fromMessageId, page);
        String compositeKey = MessageId.generateCompositeKey(userId, companionId);
        int total = messageRepository.totalSizeInDialog(compositeKey, fromMessageId);
        return mapToMessagePage(pageNumber, paging.size(), total, page);
    }

    private Slice<Message> findDialogMessages(UUID userId, UUID companionId, UUID fromMessageId, Pageable pageRequest) {
        String compositeKey = MessageId.generateCompositeKey(userId, companionId);
        if (fromMessageId == null) {
            return messageRepository.findDialogMessagesFromStart(compositeKey, pageRequest);
        }
        return messageRepository.findDialogMessagesFromMessage(compositeKey, fromMessageId, pageRequest);
    }

    private UUID setDefaultFromMessageId(UUID fromMessageId, Slice<Message> page) {
        if (fromMessageId == null) {
            fromMessageId = page.getContent().getFirst().getId().getValue();
        }
        return fromMessageId;
    }

    private Page<Message> mapToMessagePage(int pageNumber, int size, int total, Slice<Message> page) {
        Pageable pageRequest = PageRequest.of(pageNumber, size);
        return new PageImpl<>(page.getContent(), pageRequest, total);
    }

}
