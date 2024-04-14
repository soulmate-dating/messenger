package dev.glimpse.messenger.message.infrastructure;

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
                    where companions_composite_key = :companionCompositeKey
                        and id < :fromMessageId
                    order by id desc
            """)
    Slice<Message> findDialogMessagesFromMessage(@Param("companionCompositeKey") String companionCompositeKey,
                                                 UUID fromMessageId,
                                                 Pageable pageable);

    @Query(value = """
                    select * from messages
                    where companions_composite_key = :companionCompositeKey
                    order by id desc
            """)
    Slice<Message> findDialogMessagesFromStart(@Param("companionCompositeKey") String companionCompositeKey,
                                               Pageable pageable);

    @Query(value = """
                    select count(*) from messages
                    where companions_composite_key = :companionCompositeKey
                        and id < :fromMessageId
                    order by id desc
            """)
    int totalSizeInDialog(String companionCompositeKey, UUID fromMessageId);
}
