package dev.glimpse.messenger.message.entity;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@PrimaryKeyClass
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageId implements Serializable {

    @EqualsAndHashCode.Include
    @PrimaryKeyColumn(name = "id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    @NotNull
    private UUID value;

    @Getter(AccessLevel.PACKAGE)
    @PrimaryKeyColumn(name = "date", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    @NotNull
    private Instant sentTime;

    static MessageId ofRandom(MessageSentDate sentDate) {
        return new MessageId(UUID.randomUUID(), sentDate.getValue());
    }
}
