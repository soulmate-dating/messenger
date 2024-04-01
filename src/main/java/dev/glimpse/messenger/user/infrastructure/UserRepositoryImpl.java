package dev.glimpse.messenger.user.infrastructure;

import dev.glimpse.messenger.user.application.UserRepository;
import dev.glimpse.messenger.user.entity.User;
import dev.glimpse.messenger.user.entity.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final CassandraUserRepository cassandraUserRepository;

    @Override
    public Optional<User> find(UserId userId) {
        return cassandraUserRepository.findById(userId.getId());
    }

    @Override
    public void update(User user) {
        cassandraUserRepository.save(user);
    }
}
