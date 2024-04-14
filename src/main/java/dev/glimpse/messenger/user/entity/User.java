package dev.glimpse.messenger.user.entity;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Transient;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.*;
import java.util.stream.Collectors;

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

    @Setter
    @Transient
    private UserProfile profile;

    @Transient
    private UserId userId;

    protected User() {
        this.id = UserId.of().getId();
        this.userId = UserId.of(id);
        this.companions = new HashSet<>();
    }

    @Builder
    private User(UserId id, Set<UUID> companions, UserProfile profile) {
        this.id = id.getId();
        this.userId = id;
        this.companions = companions;
        this.profile = profile;
    }

    public UserId getId() {
        return UserId.of(id);
    }

    public Set<UserId> getCompanions() {
        return companions.stream()
                .map(UserId::of)
                .collect(Collectors.toUnmodifiableSet());
    }

    public void addCompanion(UserId senderId) {
        if (Objects.isNull(companions)) {
            companions = new HashSet<>();
        }
        companions.add(senderId.getId());
    }

    public Optional<UserProfile> getProfile() {
        return Optional.ofNullable(profile);
    }
}
