package net.devlab722.guestbook.gateway.backend;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableSet;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.LoadBalancerBuilder;
import com.netflix.loadbalancer.Server;

import lombok.Getter;

@Component
@Getter
public class FilterBackendLoadBalancer {

    private final ILoadBalancer loadBalancer;

    @Autowired
    FilterBackendLoadBalancer(
            @Value("${gateway.filter.endPointsAsString}") String endPointsAsString) {
        ImmutableSet<EndPoint> endPoints = ImmutableSet.copyOf(
                new LoadBalancerConfiguration(endPointsAsString, "gateway.filter.endPointsAsString")
                        .getEndPoints());
        List<Server> serverList = endPoints.stream()
                .map(e -> new Server(e.getHost(), e.getPort()))
                .collect(Collectors.toList());
        loadBalancer = LoadBalancerBuilder.newBuilder()
                .buildFixedServerListLoadBalancer(serverList);
    }

}
