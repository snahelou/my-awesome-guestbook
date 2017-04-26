package net.devlab722.guestbook.gateway.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Maps;
import com.netflix.client.DefaultLoadBalancerRetryHandler;
import com.netflix.client.RetryHandler;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.reactive.LoadBalancerCommand;

import net.devlab722.guestbook.api.Message;
import rx.Observable;

@Component
public class FilterBackend {

    @Value("${filter.sanitize.url:/api/v1/filter/sanitize}")
    public String sanitizeUrl;

    private final ILoadBalancer loadBalancer;

    private final RestTemplate restTemplate;

    // https://github.com/Netflix/ribbon/blob/master/ribbon-core/src/main/java/com/netflix/client/DefaultLoadBalancerRetryHandler.java
    // retrySameServer=0, retryNextServer=1, retryEnabled=true
    public static final RetryHandler DEFAULT_RETRY_HANDLER = new DefaultLoadBalancerRetryHandler(1, 1, true);

    @Autowired
    FilterBackend(FilterBackendLoadBalancer loadBalancerConfiguration, RestTemplate restTemplate) {
        this.loadBalancer = loadBalancerConfiguration.getLoadBalancer();
        this.restTemplate = restTemplate;
    }

    public Observable<ResponseEntity<Message>> rxFilter(Message input) {
        return LoadBalancerCommand.<ResponseEntity<Message>>builder()
                .withLoadBalancer(loadBalancer)
                .withRetryHandler(DEFAULT_RETRY_HANDLER)
                .build()
                .submit(server -> Observable.just(callRemoteService(input, server)));
    }

    ResponseEntity<Message> callRemoteService(Message message, Server server) {
        return restTemplate.exchange(
                "http://" + server.getHost() + ":" + server.getPort() + sanitizeUrl,
                HttpMethod.POST,
                new HttpEntity<>(message),
                Message.class,
                Maps.newHashMap());
    }
}
