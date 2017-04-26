package net.devlab722.guestbook.filter.backend;

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
@JsonDeserialize(builder = FilterMatch.FilterMatchBuilder.class)
public class FilterMatch {

    private final String origin;
    private final String replaced;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class FilterMatchBuilder {
    }
}
