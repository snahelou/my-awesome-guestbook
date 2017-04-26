package net.devlab722.guestbook.filter.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ContentFilter {

    private final FilterDefinition filterDefinition;

    public ContentFilter(@Autowired FilterDefinition filterDefinition) {
        this.filterDefinition = filterDefinition;

    }


    public FilteredMessage filterIfNeeded(String content) {
        boolean filtered = false;
        String newContent = content;
        if (content.isEmpty()) {
            newContent = filterDefinition.getEmptyMessageCounterMeasure();
            filtered = true;
        } else {
            for (String buzzword : filterDefinition.getBuzzWords().keySet()) {
                if (content.contains(buzzword)) {
                    filtered = true;
                    newContent = newContent.replace(buzzword, filterDefinition.getBuzzWords().get(buzzword));
                }
            }
        }
        return new FilteredMessage(newContent, filtered);
    }


    @Getter
    @ToString
    @RequiredArgsConstructor
    public static class FilteredMessage {
        private final String content;
        private final boolean filtered;
    }
}
