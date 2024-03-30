package dev.glimpse.messenger.message.application;

import dev.glimpse.messenger.message.entity.Message;
import dev.glimpse.messenger.message.entity.MessageContent;
import dev.glimpse.messenger.message.entity.MessageTag;
import dev.glimpse.messenger.user.entity.Recipient;
import dev.glimpse.messenger.user.entity.Sender;
import jakarta.annotation.Nullable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class SendingMessageUseCase {

    private final MessageRepository messageRepository;
    private final MessageSender messageSender;

    public Message execute(@NonNull UUID senderId,
                           @NonNull UUID recipientId,
                           @NonNull MessageContent content,
                           @Nullable MessageTag tag) {
        Sender sender = Sender.of(senderId);
        Recipient recipient = Recipient.of(recipientId);
        Message message = Message.builder()
                .sender(sender)
                .recipient(recipient)
                .content(content)
                .tag(tag)
                .build();
        message = messageRepository.save(message);
        messageSender.send(recipientId, message);
        return message;
    }

}
