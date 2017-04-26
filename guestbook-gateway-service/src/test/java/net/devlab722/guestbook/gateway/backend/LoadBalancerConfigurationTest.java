package net.devlab722.guestbook.gateway.backend;

import static org.assertj.core.api.Assertions.assertThat;

import org.testng.annotations.Test;

public class LoadBalancerConfigurationTest {

    @Test
    public void testGetEndPoints() throws Exception {
        LoadBalancerConfiguration lbc = new LoadBalancerConfiguration("server1:8080,server2:8080", "test");
        assertThat(lbc.getEndPoints())
                .hasSize(2)
                .contains(new EndPoint("server1", 8080))
                .contains(new EndPoint("server2", 8080));
    }

    @Test
    public void testGetEndPointsWithEmptyPort() throws Exception {
        LoadBalancerConfiguration lbc = new LoadBalancerConfiguration("server1:,server2:9090", "test");
        assertThat(lbc.getEndPoints())
                .hasSize(2)
                .contains(new EndPoint("server1", 8080))
                .contains(new EndPoint("server2", 9090));
    }

    @Test(expectedExceptions = {IllegalStateException.class})
    public void testGetEndPointsWithException() throws Exception {
        LoadBalancerConfiguration lbc = new LoadBalancerConfiguration("server1:azerty,server2:9090", "test");
        assertThat(lbc.getEndPoints())
                .hasSize(2)
                .contains(new EndPoint("server1", 8080))
                .contains(new EndPoint("server2", 9090));
    }
}