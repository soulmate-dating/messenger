package dev.glimpse.messenger.message.application;

import dev.glimpse.messenger.message.entity.Message;
import dev.glimpse.messenger.message.entity.MessageContent;
import dev.glimpse.messenger.user.entity.Recipient;
import dev.glimpse.messenger.user.entity.Sender;
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
                           @NonNull MessageContent content) {
        Sender sender = Sender.of(senderId);
        Recipient recipient = Recipient.of(recipientId);
        Message message = Message.of(sender, recipient, content);
        message = messageRepository.save(message);
        messageSender.send(recipientId, message);
        return message;
    }

}
