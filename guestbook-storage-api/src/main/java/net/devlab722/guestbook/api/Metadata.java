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
@JsonDeserialize(builder = Metadata.MetadataBuilder.class)
public class Metadata {

    private final String apiServerName;
    private final String datetimeString;
    private final String filterServerName;
    private final String filterDatetimeString;
    private final String gatewayServerName;
    private final String gatewayDatetimeString;
    private final String errorString;
    private boolean inError = false;

    public static MetadataBuilder of(Metadata original) {
        if (original != null) {
            return Metadata.builder()
                    .apiServerName(original.getApiServerName())
                    .datetimeString(original.getDatetimeString())
                    .filterServerName(original.getFilterServerName())
                    .filterDatetimeString(original.getFilterDatetimeString())
                    .gatewayServerName(original.getGatewayServerName())
                    .gatewayDatetimeString(original.gatewayDatetimeString)
                    .errorString(original.getErrorString());
        } else {
            return Metadata.builder();
        }
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static final class MetadataBuilder {
    }
}