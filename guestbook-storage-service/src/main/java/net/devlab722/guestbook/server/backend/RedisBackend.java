package net.devlab722.guestbook.server.backend;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import net.devlab722.guestbook.api.Jackson;
import net.devlab722.guestbook.api.Message;
import net.devlab722.guestbook.server.errors.BadRequestException;
import redis.clients.jedis.Jedis;

@Component
@Slf4j
public class RedisBackend {


    private Jedis jedisRead;
    private Jedis jedisWrite;
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    public static final int MAX_RETURNED_MESSAGES = 100;

    @Autowired
    public RedisBackend(
            @Value("${guestbook.backend.redis.read.vip:localhost}") String redisReadVip,
            @Value("${guestbook.backend.redis.read.port:6379}") int redisReadPort,
            @Value("${guestbook.backend.redis.write.vip:localhost}") String redisWriteVip,
            @Value("${guestbook.backend.redis.write.port:6379}") int redisWritePort) {
        log.info("Initializing Jedis Read with host <{}> and port <{}>", redisReadVip, redisReadPort);
        this.jedisRead = new Jedis(redisReadVip, redisReadPort);
        log.info("Initializing Jedis Write with host <{}> and port <{}>", redisWriteVip, redisWritePort);
        this.jedisWrite = new Jedis(redisWriteVip, redisWritePort);
    }


    public void storeMessage(Message message) {
        try {
            jedisWrite.lpush("messages", MAPPER.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            throw new BadRequestException(
                    "Caught JsonProcessingException while parsing Message [" + message + "]", e);
        }
    }

    public List<Message> getAllMessages() {

        return jedisRead.lrange("messages", 0, MAX_RETURNED_MESSAGES)
                .stream()
                .map(s -> {
                    try {
                        return MAPPER.readValue(s, Message.class);
                    } catch (IOException e) {
                        // deal with v0.0.1 message format
                        return Message.builder()
                                .content(s)
                                .build();
                    }
                })
                .collect(Collectors.toList());
    }

    public String pingReadBackend() {
        return jedisRead.ping();
    }

    public String pingWriteBackend() {
        return jedisWrite.ping();
    }
}
