package dev.glimpse.messenger.message;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MessageController {

    public record Message(String content) {
    }

    @GetMapping("/messages")
    public List<Message> getMessages() {
        return List.of(
                new Message("This is cool content")
        );
    }

}
