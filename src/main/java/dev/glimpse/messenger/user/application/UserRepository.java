package dev.glimpse.messenger.user.application;

import dev.glimpse.messenger.user.entity.User;
import dev.glimpse.messenger.user.entity.UserId;

import java.util.Optional;
import java.util.Set;

public interface UserRepository {

    Optional<User> find(UserId userId);

    Set<User> findAll(Set<UserId> userIds);

    void update(User user);

    void save(User user);

}
