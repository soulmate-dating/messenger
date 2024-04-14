package dev.glimpse.messenger.user.entity;

import java.net.URI;

public class UserProfileObjectMother {

    public static UserProfile create() {
        return new UserProfile(
                UserId.of(),
                "John",
                "Doe",
                URI.create("https://example.com/avatar.jpg")
        );
    }

}
