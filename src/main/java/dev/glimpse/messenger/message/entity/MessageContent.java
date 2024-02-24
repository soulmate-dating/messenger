package dev.glimpse.messenger.message.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.data.cassandra.core.mapping.Column;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageContent {

    @Column("content")
    private String value;

    public static MessageContent of(@NonNull String content) {
        checkContentValid(content);
        return new MessageContent(content);
    }

    private static void checkContentValid(String content) {
        if (content == null || content.isEmpty() || content.isBlank() || content.length() > 1000) {
            throw new IllegalArgumentException("Message content must not be null, empty, blank, or exceed 1000 characters.");
        }
    }
}
