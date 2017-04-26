package net.devlab722.guestbook.gateway.backend;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class EndPoint {

    private final String host;
    private final int port;

}
