package dev.glimpse.messenger.message.adapter;

import dev.glimpse.messenger.message.application.MessageRepository;
import dev.glimpse.messenger.message.entity.Message;
import dev.glimpse.messenger.message.entity.MessageId;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CassandraMessageRepository extends CassandraRepository<Message, MessageId>, MessageRepository {
}
