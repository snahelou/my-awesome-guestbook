package net.devlab722.guestbook.server.backend;

import org.springframework.stereotype.Component;

@Component
public class RedisWriteBackendHealthCheck extends RedisBackendHealthCheck {
    public static final String WRITE = "write";

    @Override
    String getPingAnswer() {
        return redisBackend.pingWriteBackend();
    }

    @Override
    String getBackendType() {
        return WRITE;
    }
}
