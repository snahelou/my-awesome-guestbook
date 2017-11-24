package net.devlab722.guestbook.api.legacy050;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@Builder
@JsonDeserialize(builder = Message050.Message050Builder.class)
public class Message050 {
    private final String content;
    private final String userName;
    private final String originalContent;
    private boolean filtered = false;
    private final Metadata050 metadata;

    public static Message050.Message050Builder of(Message050 original) {
        if (original != null) {
            return Message050.builder()
                    .content(original.getContent())
                    .filtered(original.isFiltered())
                    .userName(original.getUserName())
                    .originalContent(original.getOriginalContent())
                    .metadata(original.getMetadata());
        } else {
            return Message050.builder();
        }
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static final class Message050Builder {
    }
}
