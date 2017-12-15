package net.devlab722.guestbook.server.backend;

import org.springframework.stereotype.Component;

@Component
public class RedisReadBackendHealthCheck extends RedisBackendHealthCheck {
    public static final String READ = "read";

    @Override
    String getPingAnswer() {
        return redisBackend.pingReadBackend();
    }

    @Override
    String getBackendType() {
        return READ;
    }
}
