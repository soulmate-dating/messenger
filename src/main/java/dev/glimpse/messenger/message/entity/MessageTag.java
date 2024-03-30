package dev.glimpse.messenger.message.entity;

import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;

import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageTag {

    @Nullable
    @Column("tag_name")
    @CassandraType(type = CassandraType.Name.TEXT)
    private MessageTagName name;

    @Nullable
    @Column("tag_external_id")
    private UUID externalId;

    public static MessageTag of() {
        return new MessageTag(null, null);
    }

    public static MessageTag of(@NonNull MessageTagName name, @NonNull UUID externalId) {
        return new MessageTag(name, externalId);
    }

}
