package dev.glimpse.messenger.user.infrastructure;

import dev.glimpse.messenger.user.application.FindingUserProfilesUseCase;
import dev.glimpse.messenger.user.entity.UserId;
import dev.glimpse.messenger.user.entity.UserProfile;
import org.springframework.core.convert.ConversionService;
import profiles.ProfileServiceGrpc;
import profiles.Profiles;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class GrpcFindingUserProfilesUseCase implements FindingUserProfilesUseCase {

    private final ConversionService conversionService;
    private final ProfileServiceGrpc.ProfileServiceBlockingStub profileServiceBlockingStub;

    public GrpcFindingUserProfilesUseCase(ConversionService conversionService,
                                          ProfileServiceGrpc.ProfileServiceBlockingStub profileServiceBlockingStub) {
        this.conversionService = conversionService;
        this.profileServiceBlockingStub = profileServiceBlockingStub;
    }

    private static Profiles.GetMultipleProfilesRequest createRequest(List<String> ids) {
        return Profiles.GetMultipleProfilesRequest.newBuilder()
                .addAllIds(ids)
                .build();
    }

    @Override
    public HashMap<UserId, UserProfile> execute(Set<UserId> userId) {
        List<String> ids = userId.stream().map(UserId::getId).map(String::valueOf).toList();
        Profiles.GetMultipleProfilesRequest request = createRequest(ids);
        Profiles.MultipleProfilesResponse profiles = profileServiceBlockingStub.getMultipleProfiles(request);
        return profiles.getProfilesList().stream()
                .map(profileDto -> conversionService.convert(profileDto, UserProfile.class))
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(UserProfile::id, profile -> profile, (a, b) -> a, HashMap::new));
    }
}
