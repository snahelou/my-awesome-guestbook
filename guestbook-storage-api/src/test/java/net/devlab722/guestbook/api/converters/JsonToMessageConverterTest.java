package net.devlab722.guestbook.api.converters;

import static org.assertj.core.api.Assertions.assertThat;

import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.devlab722.guestbook.FixtureHelpers;
import net.devlab722.guestbook.api.Jackson;
import net.devlab722.guestbook.api.Message;
import net.devlab722.guestbook.api.legacy050.Metadata050;

public class JsonToMessageConverterTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    private static final JsonToMessageConverter CONVERTER = new JsonToMessageConverter(MAPPER);

    @Test
    public void should_be_impervious_to_raw_strings() throws Exception {
        Message actual = CONVERTER.apply("raw_string");
        assertThat(actual.getContent()).isEqualTo("raw_string");
    }

    @Test
    public void should_read_current_format() {
        String json = FixtureHelpers.fixture("sample_message.json");

        Message actual = CONVERTER.apply(json);
        actualShouldBeTestMessage(actual);
    }

    @Test
    public void should_read_050_format() {
        String json = FixtureHelpers.fixture("sample_message_050.json");
        Message actual = CONVERTER.apply(json);
        actualShouldBeTestMessage(actual);
        assertThat(actual.getMetadata().getConvertedFromFormat()).isEqualTo(Metadata050.VERSION);
    }

    void actualShouldBeTestMessage(Message actual) {
        assertThat(actual).isNotNull();
        assertThat(actual.getContent()).isEqualTo("Coucou ça gaze?");
        assertThat(actual.getUserName()).isEqualTo("spiderman");
        assertThat(actual.getMetadata().getStorageServerName()).isEqualTo("localhost");
        assertThat(actual.getMetadata().getStorageDatetimeString()).isEqualTo("2016-03-14T12:29:03Z");
    }

}