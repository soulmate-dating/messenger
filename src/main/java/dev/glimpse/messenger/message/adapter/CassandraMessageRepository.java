package dev.glimpse.messenger.message.adapter;

import dev.glimpse.messenger.common.Paging;
import dev.glimpse.messenger.message.application.MessageRepository;
import dev.glimpse.messenger.message.entity.Message;
import dev.glimpse.messenger.message.entity.MessageId;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Repository
public interface CassandraMessageRepository extends CassandraRepository<Message, MessageId>, MessageRepository {

    @Query(value = """
                    select * from messages
                    where recipient_id in (:companionId, :userId)
                        and sender_id in (:companionId, :userId)
                    ALLOW FILTERING
            """)
    Slice<Message> findDialogMessages(@Param("companionId") UUID companionId,
                                      @Param("userId") UUID userId,
                                      Pageable pageable);

    @Query(value = """
                    select count(*) from messages
                    where recipient_id in (:companionId, :userId)
                        and sender_id in (:companionId, :userId)
                    ALLOW FILTERING
            """)
    int totalSizeInDialog(UUID userId, UUID companionId);

    @Override
    default Page<Message> findMessagesInDialog(UUID userId, UUID companionId, Paging paging) {
        Pageable pageRequest = CassandraPageRequest.of(0, paging.size());
        Slice<Message> page = findDialogMessages(companionId, userId, pageRequest);
        int pageNumber = 0;
        while (page.hasNext() && pageNumber < paging.page()) {
            page = findDialogMessages(companionId, userId, page.nextPageable());
            pageNumber++;
        }
        if (pageNumber < paging.page()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Page not found");
        }
        int total = totalSizeInDialog(userId, companionId);
        pageRequest = PageRequest.of(pageNumber, paging.size());
        return mapToMessagePage(page.getContent(), pageRequest, total);
    }

    private static Page<Message> mapToMessagePage(List<Message> messagesInDialog, Pageable pageRequest, int totalSizeInDialog) {
        return new PageImpl<>(messagesInDialog, pageRequest, totalSizeInDialog);
    }
}
