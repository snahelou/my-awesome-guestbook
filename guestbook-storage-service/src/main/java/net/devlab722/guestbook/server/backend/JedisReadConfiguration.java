package net.devlab722.guestbook.server.backend;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jedis.read")
public class JedisReadConfiguration extends JedisConfiguration {
}
