package net.devlab722.guestbook.server.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.devlab722.guestbook.api.Version;

@Controller
@RequestMapping("/api/v1/guestbook/version")
public class VersionController {

    @Value("${guestbook.version:unknown}")
    public String version;

    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    Version echoVersion() {
        return Version.builder().storage(version).build();
    }
}
