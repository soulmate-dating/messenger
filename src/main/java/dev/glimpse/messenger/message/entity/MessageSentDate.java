package dev.glimpse.messenger.message.entity;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.data.cassandra.core.mapping.Column;

import java.time.Instant;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageSentDate {

    @NotNull
    @Column("date")
    private Instant value;

    static MessageSentDate of(@NonNull Instant value) {
        return new MessageSentDate(value);
    }

    static MessageSentDate now() {
        return new MessageSentDate(Instant.now());
    }
}
