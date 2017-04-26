package net.devlab722.guestbook.filter.backend;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.devlab722.guestbook.api.Message;
import net.devlab722.guestbook.api.Metadata;

@Component
public class FilterBackend {

    private static final String HOSTNAME = System.getenv("HOSTNAME");

    @Autowired
    public ContentFilter contentFilter;

    public Message filter(Message input) {
        ContentFilter.FilteredMessage filteredMessage = contentFilter.filterIfNeeded(input.getContent());
        return Message.of(input)
                .content(filteredMessage.getContent())
                .originalContent(input.getContent())
                .filtered(filteredMessage.isFiltered())
                .metadata(Metadata.of(input.getMetadata())
                        .filterServerName(HOSTNAME)
                        .filterDatetimeString(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()))
                        .build())
                .build();
    }
}
