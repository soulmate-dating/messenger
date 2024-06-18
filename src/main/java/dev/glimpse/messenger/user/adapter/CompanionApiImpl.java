package dev.glimpse.messenger.user.adapter;

import dev.glimpse.messenger.common.Paging;
import dev.glimpse.messenger.message.application.FindingMessagesUseCase;
import dev.glimpse.messenger.message.entity.Message;
import dev.glimpse.messenger.user.adapter.converter.composite.CompanionWithLastMessage;
import dev.glimpse.messenger.user.application.FindingCompanionsUseCase;
import dev.glimpse.messenger.user.application.FindingUserProfilesUseCase;
import dev.glimpse.messenger.user.entity.User;
import dev.glimpse.messenger.user.entity.UserId;
import dev.glimpse.messenger.user.entity.UserProfile;
import dev.glimpse.messenger.user.presentation.api.CompanionApi;
import dev.glimpse.messenger.user.presentation.dto.ChatInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class CompanionApiImpl implements CompanionApi {

    private final FindingCompanionsUseCase findingCompanionsUseCase;
    private final FindingUserProfilesUseCase findingUserProfilesUseCase;
    private final FindingMessagesUseCase findingMessagesUseCase;
    private final ConversionService conversionService;

    @Override
    public ResponseEntity<List<ChatInfoDto>> getCompanions(UUID id) {
        Set<User> companions = findingCompanionsUseCase.execute(UserId.of(id));
        enrichCompanionsProfile(companions);
        return ResponseEntity.ok(companions.stream()
                .map(companion -> new CompanionWithLastMessage(
                        companion,
                        findLastMessage(id, companion.getId())
                ))
                .map(companionWithLastMessage -> conversionService.convert(companionWithLastMessage, ChatInfoDto.class))
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(ChatInfoDto::getLastMessageSentAt).reversed())
                .toList());
    }

    private void enrichCompanionsProfile(Set<User> companions) {
        Set<UserId> ids = companions.stream().map(User::getId).collect(Collectors.toSet());
        Map<UserId, UserProfile> userProfiles = findingUserProfilesUseCase.execute(ids);
        for (User companion : companions) {
            companion.setProfile(userProfiles.getOrDefault(companion.getId(), null));
        }
    }

    private Message findLastMessage(UUID id, UserId id1) {
        return findingMessagesUseCase.execute(id, id1.getId(), null, new Paging(0, 10))
                .stream()
                .findFirst()
                .orElse(null);
    }
}
