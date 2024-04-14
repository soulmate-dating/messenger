package dev.glimpse.messenger.message.entity;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import dev.glimpse.messenger.user.entity.UserId;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode
@Getter
@PrimaryKeyClass
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageId implements Serializable {

    @Getter(AccessLevel.NONE)
    @PrimaryKeyColumn(name = "companions_composite_key", ordinal = 0, type = PrimaryKeyType.CLUSTERED)
    @NotNull
    private String companionsCompositeKey;

    @PrimaryKeyColumn(name = "id", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    @NotNull
    private UUID value;

    static MessageId ofNow(UserId senderId, UserId recipientId) {
        return new MessageId(generateCompositeKey(senderId, recipientId), Uuids.timeBased());
    }

    public static String generateCompositeKey(UserId senderId, UserId recipientId) {
        List<String> companions = new ArrayList<>() {{
            add(senderId.getId().toString());
            add(recipientId.getId().toString());
        }};
        companions.sort(String::compareTo);
        return String.join("_", companions);
    }

    @Deprecated
    public static String generateCompositeKey(UUID senderId, UUID recipientId) {
        List<String> companions = new ArrayList<>() {{
            add(senderId.toString());
            add(recipientId.toString());
        }};
        companions.sort(String::compareTo);
        return String.join("_", companions);
    }
}
