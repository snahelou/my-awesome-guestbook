package net.devlab722.guestbook.filter.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import net.devlab722.guestbook.api.Message;
import net.devlab722.guestbook.filter.backend.FilterBackend;

@Controller
@RequestMapping(path = "/api/v1/filter/sanitize")
public class FilterController {
    @Autowired
    FilterBackend filterBackend;

    @PostMapping
    public ResponseEntity<Message> storeMessage(@RequestBody(required = false) Message message) {
        return new ResponseEntity<>(filterBackend.filter(message), HttpStatus.OK);
    }
}
