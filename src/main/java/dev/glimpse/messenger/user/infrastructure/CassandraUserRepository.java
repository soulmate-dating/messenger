package dev.glimpse.messenger.user.infrastructure;

import dev.glimpse.messenger.user.entity.User;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.UUID;

public interface CassandraUserRepository extends CassandraRepository<User, UUID> {

}
