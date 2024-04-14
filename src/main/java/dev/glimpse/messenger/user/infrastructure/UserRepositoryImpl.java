package dev.glimpse.messenger.user.infrastructure;

import dev.glimpse.messenger.user.application.UserRepository;
import dev.glimpse.messenger.user.entity.User;
import dev.glimpse.messenger.user.entity.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final CassandraUserRepository cassandraUserRepository;

    @Override
    public Optional<User> find(UserId userId) {
        return cassandraUserRepository.findById(userId.getId());
    }

    @Override
    public Set<User> findAll(Set<UserId> userIds) {
        List<UUID> ids = userIds.stream().map(UserId::getId).toList();
        return new HashSet<>(cassandraUserRepository.findAllById(ids));
    }

    @Override
    public void update(User user) {
        cassandraUserRepository.save(user);
    }

    @Override
    public void save(User user) {
        cassandraUserRepository.save(user);
    }
}
