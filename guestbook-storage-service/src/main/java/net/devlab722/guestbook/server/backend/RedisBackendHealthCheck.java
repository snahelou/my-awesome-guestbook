package net.devlab722.guestbook.server.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

public abstract class RedisBackendHealthCheck implements HealthIndicator {

    public static final String EXPECTED_ANSWER = "PONG";

    @Autowired
    RedisBackend redisBackend;

    @Override
    public Health health() {
        String pingAnswer = getPingAnswer();
        if (EXPECTED_ANSWER.equals(pingAnswer)) {
            return Health.up().build();
        } else {
            return Health.down().withDetail("Message",
                    "Expected " + EXPECTED_ANSWER +
                            " from " + getBackendType() +
                            " redis endpoint, got <" + pingAnswer + ">"
            ).build();
        }
    }

    abstract String getPingAnswer();

    abstract String getBackendType();

}
