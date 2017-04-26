package net.devlab722.guestbook.server.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class RedisBackendHealthCheck implements HealthIndicator {

    public static final String EXPECTED_ANSWER = "PONG";

    @Autowired
    RedisBackend redisBackend;

    @Override
    public Health health() {
        String readPingAnswer = redisBackend.pingReadBackend();
        String writePingAnswer = redisBackend.pingWriteBackend();

        if (EXPECTED_ANSWER.equals(readPingAnswer) && EXPECTED_ANSWER.equals(writePingAnswer)) {
            return Health.up().build();
        } else {
            String errorMessage = "";
            boolean readIsInError = false;
            if (!EXPECTED_ANSWER.equals(readPingAnswer)) {
                errorMessage += "Expected " + EXPECTED_ANSWER + " from read redis endpoint, got <" + readPingAnswer + ">";
                readIsInError = true;
            }
            if (!EXPECTED_ANSWER.equals(writePingAnswer)) {
                if (readIsInError) {
                    errorMessage += "\\n";
                }
                errorMessage += "Expected " + EXPECTED_ANSWER + " from write redis endpoint, got <" + writePingAnswer + ">";
            }
            return Health.down().withDetail("Message", errorMessage).build();
        }
    }
}
