package dev.glimpse.messenger.message.entity;

import dev.glimpse.messenger.user.entity.Recipient;
import dev.glimpse.messenger.user.entity.Sender;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
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
    @Embedded.Nullable
    private Sender sender;

    @NotNull
    @Embedded.Nullable
    private Recipient recipient;

    @Deprecated
    public static Message of(@NonNull Sender sender, @NonNull Recipient recipient, @NonNull MessageContent content) {
        MessageSentDate now = MessageSentDate.now();
        return new Message(MessageId.ofRandom(now), content, MessageTag.of() , sender, recipient);
    }

    public static class MessageBuilder {
        private Sender sender;
        private Recipient recipient;
        private MessageContent content;
        private MessageTag tag;

        public MessageBuilder sender(Sender sender) {
            this.sender = sender;
            return this;
        }

        public MessageBuilder recipient(Recipient recipient) {
            this.recipient = recipient;
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

    public static MessageBuilder builder() {
        return new MessageBuilder();
    }

    private Message(MessageBuilder builder) {
        MessageSentDate now = MessageSentDate.now();
        this.id = MessageId.ofRandom(now);
        this.sender = builder.sender;
        this.recipient = builder.recipient;
        this.content = builder.content;
        this.tag = Optional.ofNullable(builder.tag).orElseGet(MessageTag::of);
    }

    public MessageSentDate getSentAt() {
        return MessageSentDate.of(id.getSentTime());
    }

    public Optional<MessageTag> getMessageTag() {
        return (tag == null || (tag.getName() == null && tag.getExternalId() == null)) ?
                Optional.empty() : Optional.of(tag);
    }
}
