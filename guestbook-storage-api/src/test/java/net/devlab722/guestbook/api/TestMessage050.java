package net.devlab722.guestbook.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import net.devlab722.guestbook.FixtureHelpers;
import net.devlab722.guestbook.api.legacy050.Message050;

@Slf4j
public class TestMessage050 {

    public static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    public static final Message050 SAMPLE_MESSAGE_050 = Message050.builder()
            .content("Coucou ça gaze?")
            .userName("spiderman")
            .metadata(TestMetadata050.SAMPLE_METADATA_050)
            .build();

    public static final Message050 SAMPLE_MESSAGE_050_CONTENT_ONLY = Message050.builder()
            .content("Oh yes!")
            .build();

    @Test
    public void testSerializationFullMessage() throws IOException {
        String json = MAPPER
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(SAMPLE_MESSAGE_050);
        log.debug("testSerializationDoesNotExplode(): Message: {}", json);
    }

    @Test
    public void testDeserializationFullMessage() throws IOException {
        String json = FixtureHelpers.fixture("sample_message_050.json");
        Message050 actual = MAPPER.readValue(json, Message050.class);
        assertThat(actual).isEqualTo(SAMPLE_MESSAGE_050);
    }

    @Test
    public void testSerializationSimpleMessage() throws IOException {
        String json = MAPPER
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(SAMPLE_MESSAGE_050_CONTENT_ONLY);
        log.debug("testSerializationDoesNotExplode(): Message with content field only: {}", json);
    }

    @Test
    public void testDeserializationSimpleMessage() throws IOException {
        String json = FixtureHelpers.fixture("sample_message_content_only.json");
        Message050 actual = MAPPER.readValue(json, Message050.class);
        assertThat(actual).isEqualTo(SAMPLE_MESSAGE_050_CONTENT_ONLY);
    }
}
