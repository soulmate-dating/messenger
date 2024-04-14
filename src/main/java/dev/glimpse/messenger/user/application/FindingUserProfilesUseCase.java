package dev.glimpse.messenger.user.application;

import dev.glimpse.messenger.user.entity.UserId;
import dev.glimpse.messenger.user.entity.UserProfile;

import java.util.Map;
import java.util.Set;

public interface FindingUserProfilesUseCase {

    Map<UserId, UserProfile> execute(Set<UserId> userId);

}
