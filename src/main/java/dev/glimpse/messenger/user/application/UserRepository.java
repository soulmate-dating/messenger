package dev.glimpse.messenger.user.application;

import dev.glimpse.messenger.user.entity.User;
import dev.glimpse.messenger.user.entity.UserId;

import java.util.Optional;

public interface UserRepository {

    Optional<User> find(UserId userId);

    void update(User user);

}
