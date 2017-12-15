package net.devlab722.guestbook.server.backend;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "backeng")
public class BackendConfiguration {
    private int maxReturnedMessages = 100;
}
