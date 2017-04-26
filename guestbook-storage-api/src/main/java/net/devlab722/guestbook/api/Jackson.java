package net.devlab722.guestbook.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Jackson {
    private Jackson() {

    }

    public static ObjectMapper newObjectMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        return configure(mapper);
    }

    private static ObjectMapper configure(ObjectMapper mapper) {
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}
