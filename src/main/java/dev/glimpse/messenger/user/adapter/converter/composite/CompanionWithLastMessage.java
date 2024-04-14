package dev.glimpse.messenger.user.adapter.converter.composite;

import dev.glimpse.messenger.message.entity.Message;
import dev.glimpse.messenger.user.entity.User;

public record CompanionWithLastMessage(
        User companion,
        Message lastMessage
) {
}
