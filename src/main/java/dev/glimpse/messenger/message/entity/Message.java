package dev.glimpse.messenger.message.entity;

import dev.glimpse.messenger.user.entity.UserId;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.cassandra.core.mapping.Embedded;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Optional;

@Table("messages")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Nullable
    @Embedded.Empty
    private MessageTag tag;

    @NotNull
    @Embedded(prefix = "sender_", onEmpty = Embedded.OnEmpty.USE_NULL)
    private UserId senderId;

    @NotNull
    @Embedded(prefix = "recipient_", onEmpty = Embedded.OnEmpty.USE_NULL)
    private UserId recipientId;

    private Message(MessageBuilder builder) {
        MessageSentDate now = MessageSentDate.now();
        this.id = MessageId.ofRandom(now);
        this.senderId = builder.senderId;
        this.recipientId = builder.recipientId;
        this.content = builder.content;
        this.tag = Optional.ofNullable(builder.tag).orElseGet(MessageTag::of);
    }

    @Deprecated
    public static Message of(@NonNull UserId senderId, @NonNull UserId recipient, @NonNull MessageContent content) {
        MessageSentDate now = MessageSentDate.now();
        return new Message(MessageId.ofRandom(now), content, MessageTag.of(), senderId, recipient);
    }

    public static MessageBuilder builder() {
        return new MessageBuilder();
    }

    public static class MessageBuilder {
        private UserId senderId;
        private UserId recipientId;
        private MessageContent content;
        private MessageTag tag;

        public MessageBuilder senderId(UserId senderId) {
            this.senderId = senderId;
            return this;
        }

        public MessageBuilder recipientId(UserId recipientId) {
            this.recipientId = recipientId;
            return this;
        }

        public MessageBuilder content(MessageContent content) {
            this.content = content;
            return this;
        }

        public MessageBuilder tag(MessageTag tag) {
            this.tag = tag;
            return this;
        }

        public Message build() {
            return new Message(this);
        }
    }

    public MessageSentDate getSentAt() {
        return MessageSentDate.of(id.getSentTime());
    }

    public Optional<MessageTag> getMessageTag() {
        return (tag == null || (tag.getName() == null && tag.getExternalId() == null)) ?
                Optional.empty() : Optional.of(tag);
    }
}
