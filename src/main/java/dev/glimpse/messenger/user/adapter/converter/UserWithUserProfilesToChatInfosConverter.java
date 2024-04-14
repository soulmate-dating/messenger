package dev.glimpse.messenger.user.adapter.converter;

import dev.glimpse.messenger.user.adapter.converter.composite.CompanionWithLastMessage;
import dev.glimpse.messenger.user.entity.UserProfile;
import dev.glimpse.messenger.user.presentation.dto.ChatInfoDto;
import dev.glimpse.messenger.user.presentation.dto.CompanionDto;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserWithUserProfilesToChatInfosConverter implements
        Converter<CompanionWithLastMessage, ChatInfoDto> {

    @Override
    public ChatInfoDto convert(@NonNull CompanionWithLastMessage source) {
        return new ChatInfoDto()
                .companion(convertCompanionDto(source))
                .lastMessage(source.lastMessage().getContent().getValue())
                .isYourTurn(source.lastMessage().getSenderId().equals(source.companion().getId()));
    }

    private CompanionDto convertCompanionDto(CompanionWithLastMessage source) {
        return new CompanionDto()
                .id(source.companion().getId().getId())
                .firstName(convertFirstName(source))
                .lastName(convertLastName(source))
                .picLink(convertPicLink(source));
    }

    private String convertFirstName(CompanionWithLastMessage source) {
        return source.companion().getProfile()
                .map(UserProfile::firstName)
                .orElseGet(() -> {
                    log.error("Failed to found user profile for user with id: {}", source.companion().getId().getId());
                    return "unknown";
                });
    }

    private String convertLastName(CompanionWithLastMessage source) {
        return source.companion().getProfile()
                .map(UserProfile::lastName)
                .orElseGet(() -> {
                    log.error("Failed to found user profile for user with id: {}", source.companion().getId().getId());
                    return "user";
                });
    }

    private String convertPicLink(CompanionWithLastMessage source) {
        return source.companion().getProfile()
                .map(UserProfile::avatarLink)
                .map(Object::toString)
                .filter(link -> !link.isBlank())
                .orElse(null);
    }

}
