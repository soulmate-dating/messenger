package dev.glimpse.messenger.user.entity;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Transient;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.*;

@Table("users")
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @NotNull
    @EqualsAndHashCode.Include
    @PrimaryKey("id")
    private UUID id;

    @Column("companions")
    private Set<UUID> companions;

    @Transient
    private UserId userId;

    protected User() {
        this.id = UserId.of().getId();
        this.userId = UserId.of(id);
        this.companions = new HashSet<>();
    }

    @Builder
    private User(UserId id, Set<UUID> companions) {
        this.id = id.getId();
        this.userId = id;
        this.companions = companions;
    }

    public UserId getId() {
        return userId;
    }

    public Set<UUID> getCompanions() {
        return Collections.unmodifiableSet(companions);
    }

    public void addCompanion(UserId senderId) {
        if (Objects.isNull(companions)) {
            companions = new HashSet<>();
        }
        companions.add(senderId.getId());
    }
}
