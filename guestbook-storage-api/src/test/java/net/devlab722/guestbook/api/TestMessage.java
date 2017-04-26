package net.devlab722.guestbook.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import net.devlab722.guestbook.FixtureHelpers;

@Slf4j
public class TestMessage {

    public static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    public static final Message SAMPLE_MESSAGE = Message.builder()
            .content("Coucou ça gaze?")
            .userName("spiderman")
            .metadata(TestMetadata.SAMPLE_METADATA)
            .build();

    public static final Message SAMPLE_MESSAGE_CONTENT_ONLY = Message.builder()
            .content("Oh yes!")
            .build();

    @Test
    public void testSerializationFullMessage() throws IOException {
        String json = MAPPER
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(SAMPLE_MESSAGE);
        log.debug("testSerializationDoesNotExplode(): Message: {}", json);
    }

    @Test
    public void testDeserializationFullMessage() throws IOException {
        String json = FixtureHelpers.fixture("sample_message.json");
        Message actual = MAPPER.readValue(json, Message.class);
        assertThat(actual).isEqualTo(SAMPLE_MESSAGE);
    }

    @Test
    public void testSerializationSimpleMessage() throws IOException {
        String json = MAPPER
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(SAMPLE_MESSAGE_CONTENT_ONLY);
        log.debug("testSerializationDoesNotExplode(): Message with content field only: {}", json);
    }

    @Test
    public void testDeserializationSimpleMessage() throws IOException {
        String json = FixtureHelpers.fixture("sample_message_content_only.json");
        Message actual = MAPPER.readValue(json, Message.class);
        assertThat(actual).isEqualTo(SAMPLE_MESSAGE_CONTENT_ONLY);
    }
}
