package dev.glimpse.messenger.message.entity;

import dev.glimpse.messenger.user.entity.Recipient;
import dev.glimpse.messenger.user.entity.Sender;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.springframework.data.cassandra.core.mapping.Embedded;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("messages")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Message {

    @EqualsAndHashCode.Include
    @NotNull
    @PrimaryKey
    private MessageId id;

    @NotNull
    @Embedded.Nullable
    private MessageContent content;

    @NotNull
    @Embedded.Nullable
    private Sender sender;

    @NotNull
    @Embedded.Nullable
    private Recipient recipient;

    public static Message of(@NonNull Sender sender, @NonNull Recipient recipient, @NonNull MessageContent content) {
        MessageSentDate now = MessageSentDate.now();
        return new Message(MessageId.ofRandom(now), content, sender, recipient);
    }

    public MessageSentDate getSentAt() {
        return MessageSentDate.of(id.getSentTime());
    }

}
