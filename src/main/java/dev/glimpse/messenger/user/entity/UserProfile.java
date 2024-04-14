package dev.glimpse.messenger.user.entity;

import java.net.URI;

public record UserProfile(
        UserId id,
        String firstName,
        String lastName,
        URI avatarLink
) {

}
