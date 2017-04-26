package net.devlab722.guestbook.gateway.backend;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Map;
import java.util.stream.Collector;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class LoadBalancerConfiguration {
    private final ImmutableSet<EndPoint> endPoints;

    LoadBalancerConfiguration(
            String endPointsAsString, String context) {
        checkNotNull(
                endPointsAsString,
                "the value of the endPointsAsString in context <" + context + "> cannot be null");
        checkState(
                !endPointsAsString.isEmpty(),
                "the value of the endPointsAsString in context <" + context + "> cannot be empty");
        Map<String, String> splitted =
                Splitter.on(',')
                        .omitEmptyStrings()
                        .trimResults()
                        .withKeyValueSeparator(':')
                        .split(endPointsAsString);
        endPoints = splitted.entrySet().stream()
                .map(e -> {
                    if (e.getValue() != null && !e.getValue().isEmpty()) {
                        try {
                            return new EndPoint(e.getKey(), Integer.parseInt(e.getValue()));
                        } catch (NumberFormatException nfe) {
                            String message = "The value <" + e.getValue() + "> is not a valid port value";
                            log.error(message);
                            throw new IllegalStateException(message, nfe);
                        }
                    } else {
                        return new EndPoint(e.getKey(), 8080);
                    }
                }).collect(
                        Collector.of(ImmutableSet.Builder<EndPoint>::new,
                                ImmutableSet.Builder<EndPoint>::add,
                                (l, r) -> l.addAll(r.build()),
                                ImmutableSet.Builder<EndPoint>::build));
    }
}

