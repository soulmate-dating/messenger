package dev.glimpse.messenger.user.entity;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.data.cassandra.core.mapping.Column;

import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserId {

    @Column("id")
    @NotNull
    private UUID id;

    public static UserId of(@NonNull UUID id) {
        return new UserId(id);
    }

}