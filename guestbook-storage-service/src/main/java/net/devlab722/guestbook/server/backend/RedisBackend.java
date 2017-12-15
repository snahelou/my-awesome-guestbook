package net.devlab722.guestbook.server.backend;

import java.time.Duration;
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
import net.devlab722.guestbook.api.converters.JsonToMessageConverter;
import net.devlab722.guestbook.server.errors.BadRequestException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Component
@Slf4j
public class RedisBackend {


    private JedisPool jedisReadPool;
    private JedisPool jedisWritePool;
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    private static final JsonToMessageConverter CONVERTER = new JsonToMessageConverter(MAPPER);
    private BackendConfiguration backendConfiguration;

    @Autowired
    public RedisBackend(
            @Value("${guestbook.backend.redis.read.vip:localhost}") String redisReadVip,
            @Value("${guestbook.backend.redis.read.port:6379}") int redisReadPort,
            @Value("${guestbook.backend.redis.write.vip:localhost}") String redisWriteVip,
            @Value("${guestbook.backend.redis.write.port:6379}") int redisWritePort,
            JedisWriteConfiguration jedisWriteConfiguration,
            JedisReadConfiguration jedisReadConfiguration,
            BackendConfiguration backendConfiguration) {
        log.info("Initializing Jedis Read with host <{}> , port <{}> and configuration <{}>",
                redisReadVip,
                redisReadPort,
                jedisReadConfiguration);
        this.jedisReadPool = new JedisPool(buildPoolConfig(jedisReadConfiguration), redisReadVip, redisReadPort);
        log.info("Testing Jedis Read Connection with ping => <{}>", pingReadBackend());
        log.info("Initializing Jedis Write with host <{}>, port <{}> and configuration <{}>",
                redisWriteVip,
                redisWritePort,
                jedisWriteConfiguration);
        this.jedisWritePool = new JedisPool(buildPoolConfig(jedisWriteConfiguration), redisWriteVip, redisWritePort);
        log.info("Testing Jedis Write Connection with ping => <{}>", pingWriteBackend());
        this.backendConfiguration = backendConfiguration;
        log.info("Backend configuration <{}>", backendConfiguration);
    }


    public void storeMessage(Message message) {
        try {
            try (Jedis jedisWrite = jedisWritePool.getResource()) {
                jedisWrite.lpush("messages", MAPPER.writeValueAsString(message));
            }
        } catch (JsonProcessingException e) {
            throw new BadRequestException(
                    "Caught JsonProcessingException while parsing Message [" + message + "]", e);
        }
    }

    public List<Message> getAllMessages() {
        try (Jedis jedisRead = jedisReadPool.getResource()) {
            return jedisRead.lrange("messages", 0, backendConfiguration.getMaxReturnedMessages())
                    .stream()
                    .map(CONVERTER)
                    .collect(Collectors.toList());
        }
    }

    private JedisPoolConfig buildPoolConfig(JedisConfiguration jedisConfiguration) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(jedisConfiguration.getMaxTotal());
        poolConfig.setMaxIdle(jedisConfiguration.getMaxIdle());
        poolConfig.setMinIdle(jedisConfiguration.getMinIdle());
        poolConfig.setTestOnBorrow(jedisConfiguration.isTestOnBorrow());
        poolConfig.setTestOnReturn(jedisConfiguration.isTestOnReturn());
        poolConfig.setTestWhileIdle(jedisConfiguration.isTestWhileIdle());
        poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(jedisConfiguration.getMinEvictableIdleTimeSeconds()).toMillis());
        poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(jedisConfiguration.getTimeBetweenEvictionRunsSeconds()).toMillis());
        poolConfig.setNumTestsPerEvictionRun(jedisConfiguration.getNumTestsPerEvictionRun());
        poolConfig.setBlockWhenExhausted(jedisConfiguration.isBlockWhenExhausted());
        return poolConfig;
    }

    public String pingReadBackend() {
        try (Jedis jedisRead = jedisReadPool.getResource()) {
            return jedisRead.ping();
        }
    }

    public String pingWriteBackend() {
        try (Jedis jedisWrite = jedisWritePool.getResource()) {
            return jedisWrite.ping();
        }
    }
}
