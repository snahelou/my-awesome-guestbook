package net.devlab722.guestbook.filter.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.devlab722.guestbook.api.Version;

@Controller
@RequestMapping("/api/v1/filter/version")
public class VersionController {

    @Value("${filter.version:unknown}")
    public String filterVersion;

    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    Version echoVersion() {
        return Version.builder()
                .filter(filterVersion)
                .build();
    }
}
