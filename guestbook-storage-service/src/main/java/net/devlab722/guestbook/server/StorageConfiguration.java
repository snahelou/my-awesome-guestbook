package net.devlab722.guestbook.server;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import net.devlab722.guestbook.server.backend.BackendConfiguration;
import net.devlab722.guestbook.server.backend.JedisReadConfiguration;
import net.devlab722.guestbook.server.backend.JedisWriteConfiguration;

@Configuration
@EnableConfigurationProperties({
        JedisReadConfiguration.class,
        JedisWriteConfiguration.class,
        BackendConfiguration.class})
public class StorageConfiguration {
}
