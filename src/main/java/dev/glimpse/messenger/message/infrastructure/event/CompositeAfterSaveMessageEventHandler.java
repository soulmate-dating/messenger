package dev.glimpse.messenger.message.infrastructure.event;

import dev.glimpse.messenger.message.application.event.AfterSaveMessageEventHandler;
import dev.glimpse.messenger.message.entity.Message;
import lombok.NonNull;
import org.springframework.data.cassandra.core.mapping.event.AbstractCassandraEventListener;
import org.springframework.data.cassandra.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public class CompositeAfterSaveMessageEventHandler extends AbstractCassandraEventListener<Message> {

    private final List<AfterSaveMessageEventHandler> afterSaveMessageEventHandlers;

    public CompositeAfterSaveMessageEventHandler(List<AfterSaveMessageEventHandler> afterSaveMessageEventHandlers) {
        if (Objects.isNull(afterSaveMessageEventHandlers)) {
            afterSaveMessageEventHandlers = Collections.emptyList();
        }
        this.afterSaveMessageEventHandlers = afterSaveMessageEventHandlers;
    }

    @Override
    public void onAfterSave(@NonNull AfterSaveEvent<Message> event) {
        afterSaveMessageEventHandlers.forEach((handler) -> handler.handleAfterSave(event.getSource()));
    }

}
