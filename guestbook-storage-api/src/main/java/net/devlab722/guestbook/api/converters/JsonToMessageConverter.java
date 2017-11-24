package net.devlab722.guestbook.api.converters;

import java.io.IOException;
import java.util.function.Function;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.devlab722.guestbook.api.Message;
import net.devlab722.guestbook.api.legacy050.Message050;

public class JsonToMessageConverter implements Function<String, Message> {

    private final ObjectMapper mapper;

    public JsonToMessageConverter(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Message apply(String s) {
        try {
            // try to convert string (should be json) to current Message/Metadata format
            return mapper.readValue(s, Message.class);
        } catch (IOException e) {
            try {
                // if it fails, try to convert to previous known format (0.5.0)
                Message050 message050 = mapper.readValue(s, Message050.class);
                return message050.toCurrent();
            } catch (IOException e1) {
                // if it fails, fallback to raw string
                return Message.builder()
                        .content(s)
                        .build();
            }
        }
    }
}
