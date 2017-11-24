package net.devlab722.guestbook.api.converters;

import java.io.IOException;
import java.util.function.Function;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.devlab722.guestbook.api.Message;
import net.devlab722.guestbook.api.Metadata;
import net.devlab722.guestbook.api.legacy050.Message050;

public class JsonToMessageConverter implements Function<String, Message> {

    private final ObjectMapper mapper;

    public JsonToMessageConverter(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Message apply(String s) {
        try {
            return mapper.readValue(s, Message.class);
        } catch (IOException e) {
            try {
                Message050 message050 = mapper.readValue(s, Message050.class);
            } catch (IOException e1) {
                return Message.builder()
                        .content(s)
                        .build();
            }
        }
        return Message.builder()
                .content("")
                .metadata(
                        Metadata.builder()
                                .errorString("Could not parse message <" + s + ">")
                                .build())
                .build();
    }
}
