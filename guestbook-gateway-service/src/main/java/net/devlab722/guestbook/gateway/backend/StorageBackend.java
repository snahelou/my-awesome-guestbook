package net.devlab722.guestbook.gateway.backend;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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
public class StorageBackend {

    @Value("${guestbook.messages.url:/api/v1/guestbook/messages}")
    public String guestbookMessagesUrl;

    @Value("${filter.version.url:/api/v1/guestbook/version}")
    public String versionUrl;

    private final ILoadBalancer loadBalancer;

    private final RestTemplate restTemplate;

    private final RetryHandler retryHandler;

    private static final ParameterizedTypeReference<List<Message>> LIST_OF_MESSAGES_TYPE_REF =
            new ParameterizedTypeReference<List<Message>>() {
            };

    @Autowired
    StorageBackend(
            ApiBackendLoadBalancer loadBalancerConfiguration,
            RestTemplate restTemplate,
            RetryHandler retryHandler) {
        this.loadBalancer = loadBalancerConfiguration.getLoadBalancer();
        this.restTemplate = restTemplate;
        this.retryHandler = retryHandler;
    }

    public Observable<ResponseEntity<Message>> rxStore(Message input) {
        return LoadBalancerCommand
                .<ResponseEntity<Message>>builder()
                .withLoadBalancer(loadBalancer)
                .withRetryHandler(retryHandler)
                .build()
                .submit(
                        server -> Observable.just(callRemoteStoreService(input, server))
                );
    }

    ResponseEntity<Message> callRemoteStoreService(Message message, Server server) {
        return restTemplate.exchange(
                "http://" + server.getHost() + ":" + server.getPort() + guestbookMessagesUrl,
                HttpMethod.POST,
                new HttpEntity<>(message),
                Message.class,
                Maps.newHashMap());
    }


    public Observable<ResponseEntity<List<Message>>> rxGet() {
        return LoadBalancerCommand
                .<ResponseEntity<List<Message>>>builder()
                .withLoadBalancer(loadBalancer)
                .withRetryHandler(retryHandler)
                .build()
                .submit(
                        server -> Observable.just(callRemoteGetService(server))
                );
    }

    ResponseEntity<List<Message>> callRemoteGetService(Server server) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        return restTemplate.exchange(
                "http://" + server.getHost() + ":" + server.getPort() + guestbookMessagesUrl,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                LIST_OF_MESSAGES_TYPE_REF,
                Maps.newHashMap());
    }

    public Observable<ResponseEntity<Version>> rxStorageVersion() {
        return LoadBalancerCommand.<ResponseEntity<Version>>builder()
                .withLoadBalancer(loadBalancer)
                .withRetryHandler(retryHandler)
                .build()
                .submit(server -> Observable.just(getStorageServiceVersion(server)));
    }


    ResponseEntity<Version> getStorageServiceVersion(Server server) {
        return restTemplate.exchange(
                "http://" + server.getHost() + ":" + server.getPort() + versionUrl,
                HttpMethod.GET,
                null,
                Version.class,
                Maps.newHashMap());
    }

}
