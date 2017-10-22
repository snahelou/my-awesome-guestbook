package net.devlab722.guestbook.gateway.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import lombok.extern.slf4j.Slf4j;
import net.devlab722.guestbook.api.Message;
import net.devlab722.guestbook.api.Metadata;
import net.devlab722.guestbook.gateway.backend.StorageBackend;
import net.devlab722.guestbook.gateway.backend.FilterBackend;

@Controller
@RequestMapping(path = "/api/v1/guestbook/messages")
@Slf4j
public class GatewayController {

    private static final String HOSTNAME = System.getenv("HOSTNAME");

    @Autowired
    FilterBackend filterBackend;

    @Autowired
    StorageBackend storageBackend;

    @RequestMapping(method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity<Message> storeMessage(@RequestBody(required = true) Message message) {
        Set<ResponseEntity<Message>> result = Sets.newHashSet();
        // Call the filter service
        Message augmented =  Message.of(message).metadata(
                Metadata.of(message.getMetadata())
                        .gatewayServerName(HOSTNAME)
                        .gatewayDatetimeString(
                                DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()))
                        .build()
        ).build();
        filterBackend.rxFilter(augmented)
                .map(rem -> {
                    if (rem.getStatusCode() != HttpStatus.OK) {
                        throw new RuntimeException("Expected status code <" +
                                HttpStatus.OK + "> from the filter service, got <" +
                                rem.getStatusCode() + ">");
                    } else {
                        return rem;
                    }
                })
                .flatMap(rem -> storageBackend.rxStore(rem.getBody()))
                .map(rem -> {
                    if (rem.getStatusCode() != HttpStatus.CREATED) {
                        throw new RuntimeException("Expected status code <" +
                                HttpStatus.CREATED + "> from the api service, got <" +
                                rem.getStatusCode() + ">");
                    } else {
                        return rem;
                    }
                })
                .first()
                .subscribe(result::add,
                        error -> {
                            Message errorMessage = Message.of(message)
                                    .metadata(
                                            Metadata.builder()
                                                    .inError(true)
                                                    .errorString(error.getMessage())
                                                    .build()
                                    ).build();
                            result.add(new ResponseEntity<>(errorMessage, HttpStatus.SERVICE_UNAVAILABLE));
                        });

        return result.stream().findFirst().get();
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<Message>> getMessages() {
        Set<ResponseEntity<List<Message>>> result = Sets.newHashSet();
        // call the gestbook api service to get all messages
        storageBackend.rxGet()
                .map(rem -> {
                    if (rem.getStatusCode() != HttpStatus.OK) {
                        throw new RuntimeException("Expected status code <" +
                                HttpStatus.OK + "> from the api service, got <" +
                                rem.getStatusCode() + ">");
                    } else {
                        return rem;
                    }
                })
                .first()
                .subscribe(result::add,
                        error -> {
                            Message errorMessage = Message.builder()
                                    .metadata(
                                            Metadata.builder().inError(true).errorString(error.getMessage()).build()
                                    ).build();
                            result.add(new ResponseEntity<>(Lists.newArrayList(), HttpStatus.BAD_REQUEST));
                        });
        return result.stream().findFirst().get();
    }


}
