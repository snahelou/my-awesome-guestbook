package net.devlab722.guestbook.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import net.devlab722.guestbook.FixtureHelpers;

@Slf4j
public class TestMetadata {
    public static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    public static final Metadata SAMPLE_METADATA = Metadata.builder()
            .apiServerName("localhost")
            .datetimeString("2016-03-14T12:29:03Z")
            .build();

    @Test
    public void testSerialization() throws IOException {
        String json = MAPPER
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(SAMPLE_METADATA);
        log.debug("testSerializationDoesNotExplode(): Metadata: {}", json);
    }

    @Test
    public void testDeserialization() throws IOException {
        String json = FixtureHelpers.fixture("sample_metadata.json");
        Metadata actual = MAPPER.readValue(json, Metadata.class);
        assertThat(actual).isEqualTo(SAMPLE_METADATA);
    }

}