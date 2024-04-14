package dev.glimpse.messenger.user.application;

import dev.glimpse.messenger.user.entity.User;
import dev.glimpse.messenger.user.entity.UserId;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@RequiredArgsConstructor
@Component
public class FindingCompanionsUseCase {

    private final UserRepository userRepository;

    public Set<User> execute(@NonNull UserId id) {
        User user = userRepository.find(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return userRepository.findAll(user.getCompanions());
    }

}
