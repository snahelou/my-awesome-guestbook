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
@JsonDeserialize(builder = Version.VersionBuilder.class)
public class Version {
    private final String product;
    private final String gateway;
    private final String filter;
    private final String storage;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class VersionBuilder {
    }
}
