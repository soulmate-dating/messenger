package dev.glimpse.messenger.message.adapter;

import dev.glimpse.messenger.message.entity.Message;
import dev.glimpse.messenger.message.entity.MessageId;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CassandraMessageRepository extends CassandraRepository<Message, MessageId> {

    @Query(value = """
                    select * from messages
                    where recipient_id in (:companionId, :userId)
                        and sender_id in (:companionId, :userId)
                        and token(id) >= token(:fromMessageId)
                    ALLOW FILTERING
            """)
    Slice<Message> findDialogMessagesFromMessage(@Param("companionId") UUID companionId,
                                                 @Param("userId") UUID userId,
                                                 UUID fromMessageId,
                                                 Pageable pageable);

    @Query(value = """
                    select * from messages
                    where recipient_id in (:companionId, :userId)
                        and sender_id in (:companionId, :userId)
                    ALLOW FILTERING
            """)
    Slice<Message> findDialogMessagesFromStart(@Param("companionId") UUID companionId,
                                               @Param("userId") UUID userId,
                                               Pageable pageable);

    @Query(value = """
                    select count(*) from messages
                    where recipient_id in (:companionId, :userId)
                        and sender_id in (:companionId, :userId)
                        and token(id) >= token(:fromMessageId)
                    ALLOW FILTERING
            """)
    int totalSizeInDialog(UUID userId, UUID companionId, UUID fromMessageId);

}
