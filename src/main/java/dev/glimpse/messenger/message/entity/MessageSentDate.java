package dev.glimpse.messenger.message.entity;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.time.Instant;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageSentDate {

    @NotNull
    private Instant value;

    static MessageSentDate of(@NonNull Instant value) {
        return new MessageSentDate(value);
    }

    static MessageSentDate now() {
        return new MessageSentDate(Instant.now());
    }
}
