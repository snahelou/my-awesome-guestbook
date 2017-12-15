package net.devlab722.guestbook.server.backend;

import lombok.Data;

@Data
public class JedisConfiguration {
    private int maxTotal = 5;
    private int maxIdle = 2;
    private int minIdle = 1;
    private boolean testOnBorrow = true;
    private boolean testOnReturn = true;
    private boolean testWhileIdle = true;
    private int minEvictableIdleTimeSeconds = 60;
    private int timeBetweenEvictionRunsSeconds = 30;
    private int numTestsPerEvictionRun = 3;
    private boolean blockWhenExhausted = true;
}
