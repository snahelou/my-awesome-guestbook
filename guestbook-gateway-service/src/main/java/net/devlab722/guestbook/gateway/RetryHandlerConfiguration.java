package net.devlab722.guestbook.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.netflix.client.DefaultLoadBalancerRetryHandler;
import com.netflix.client.RetryHandler;

@Configuration
public class RetryHandlerConfiguration {

    @Value("${gateway.rest.retry.same.server:1}")
    public int retrySameServer;

    @Value("${gateway.rest.retry.next.server:1}")
    public int retryNextServer;

    @Value("${gateway.rest.retry.enabled:true}")
    public boolean retryEnabled;

    @Bean
    public RetryHandler configureRetryHandler() {
        // https://github.com/Netflix/ribbon/blob/master/ribbon-core/src/main/java/com/netflix/client/DefaultLoadBalancerRetryHandler.java
        // retrySameServer=0, retryNextServer=1, retryEnabled=true

        return new DefaultLoadBalancerRetryHandler(retrySameServer, retryNextServer, retryEnabled);
    }
}
