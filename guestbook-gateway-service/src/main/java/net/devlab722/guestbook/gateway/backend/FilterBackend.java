package net.devlab722.guestbook.gateway.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Maps;
import com.netflix.client.RetryHandler;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.reactive.LoadBalancerCommand;

import net.devlab722.guestbook.api.Message;
import net.devlab722.guestbook.api.Version;
import rx.Observable;

@Component
public class FilterBackend {

    @Value("${filter.sanitize.url:/api/v1/filter/sanitize}")
    public String sanitizeUrl;

    @Value("${filter.version.url:/api/v1/filter/version}")
    public String versionUrl;

    private final ILoadBalancer loadBalancer;

    private final RestTemplate restTemplate;

    private final RetryHandler retryHandler;


    @Autowired
    FilterBackend(
            FilterBackendLoadBalancer loadBalancerConfiguration,
            RestTemplate restTemplate,
            RetryHandler retryHandler
    ) {
        this.loadBalancer = loadBalancerConfiguration.getLoadBalancer();
        this.restTemplate = restTemplate;
        this.retryHandler = retryHandler;
    }

    public Observable<ResponseEntity<Message>> rxFilter(Message input) {
        return LoadBalancerCommand.<ResponseEntity<Message>>builder()
                .withLoadBalancer(loadBalancer)
                .withRetryHandler(retryHandler)
                .build()
                .submit(server -> Observable.just(callRemoteService(input, server)));
    }

    public Observable<ResponseEntity<Version>> rxFilterVersion() {
        return LoadBalancerCommand.<ResponseEntity<Version>>builder()
                .withLoadBalancer(loadBalancer)
                .withRetryHandler(retryHandler)
                .build()
                .submit(server -> Observable.just(getFilterServiceVersion(server)));
    }

    ResponseEntity<Message> callRemoteService(Message message, Server server) {
        return restTemplate.exchange(
                "http://" + server.getHost() + ":" + server.getPort() + sanitizeUrl,
                HttpMethod.POST,
                new HttpEntity<>(message),
                Message.class,
                Maps.newHashMap());
    }

    ResponseEntity<Version> getFilterServiceVersion(Server server) {
        return restTemplate.exchange(
                "http://" + server.getHost() + ":" + server.getPort() + versionUrl,
                HttpMethod.GET,
                null,
                Version.class,
                Maps.newHashMap());
    }
}
