package net.devlab722.guestbook.api;

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
@JsonDeserialize(builder = Message.MessageBuilder.class)
public class Message {
    private final String content;
    private final String userName;
    private final String originalContent;
    private boolean filtered = false;
    private final Metadata metadata;

    public static MessageBuilder of(Message original) {
        if (original != null) {
            return Message.builder()
                    .content(original.getContent())
                    .filtered(original.isFiltered())
                    .userName(original.getUserName())
                    .originalContent(original.getOriginalContent())
                    .metadata(original.getMetadata());
        } else {
            return Message.builder();
        }
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static final class MessageBuilder {
    }
}
