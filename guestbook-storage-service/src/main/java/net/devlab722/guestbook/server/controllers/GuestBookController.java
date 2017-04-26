package net.devlab722.guestbook.server.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.devlab722.guestbook.api.Message;
import net.devlab722.guestbook.api.Metadata;
import net.devlab722.guestbook.server.backend.RedisBackend;

@Controller
@RequestMapping(path = "/api/v1/guestbook/messages")
public class GuestBookController {

    @Autowired
    RedisBackend redisBackend;

    private static final String HOSTNAME = System.getenv("HOSTNAME");

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Message> storeMessage(@RequestBody(required = true) Message message) {
        Message augmented = Message.of(message).metadata(
                Metadata.of(
                        message.getMetadata())
                        .apiServerName(HOSTNAME)
                        .datetimeString(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()))
                        .build()
        ).build();
        redisBackend.storeMessage(augmented);
        return new ResponseEntity<>(augmented, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Message> getMessages() {
        return redisBackend.getAllMessages();
    }

}
