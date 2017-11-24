package net.devlab722.guestbook.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import net.devlab722.guestbook.FixtureHelpers;
import net.devlab722.guestbook.api.legacy050.Metadata050;

@Slf4j
public class TestMetadata050 {
    public static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    public static final Metadata050 SAMPLE_METADATA_050 = Metadata050.builder()
            .apiServerName("localhost")
            .datetimeString("2016-03-14T12:29:03Z")
            .build();

    @Test
    public void testSerialization() throws IOException {
        String json = MAPPER
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(SAMPLE_METADATA_050);
        log.debug("testSerializationDoesNotExplode(): Metadata050: {}", json);
    }

    @Test
    public void testDeserialization() throws IOException {
        String json = FixtureHelpers.fixture("sample_metadata_050.json");
        Metadata050 actual = MAPPER.readValue(json, Metadata050.class);
        assertThat(actual).isEqualTo(SAMPLE_METADATA_050);
    }

}