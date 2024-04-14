package dev.glimpse.messenger.user.infrastructure.converter;

import dev.glimpse.messenger.user.entity.UserId;
import dev.glimpse.messenger.user.entity.UserProfile;
import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import profiles.Profiles;

import java.net.URI;
import java.util.UUID;

@Component
public class ProfileResponseToUserProfileConverter implements Converter<Profiles.ProfileResponse, UserProfile> {
    private static UserId convertUserId(Profiles.ProfileResponse source) {
        return UserId.of(UUID.fromString(source.getId()));
    }

    private static URI convertAvatarLink(Profiles.ProfileResponse source) {
        return URI.create(source.getPersonalInfo().getProfilePicLink());
    }

    @Override
    public UserProfile convert(@NonNull Profiles.ProfileResponse source) {
        return new UserProfile(
                convertUserId(source),
                source.getPersonalInfo().getFirstName(),
                source.getPersonalInfo().getLastName(),
                convertAvatarLink(source)
        );
    }
}
