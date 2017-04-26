package net.devlab722.guestbook.gateway.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.devlab722.guestbook.api.Version;

@Controller
@RequestMapping("/api/v1/version")
public class VersionController {

    @Value("${product.version:unknown}")
    public String version;

    @Value("${gateway.version:unknown}")
    public String gatewayVersion;

    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    Version echoVersion() {
        return Version.builder()
                .version(version)
                .gateway(gatewayVersion)
                .build();
    }
}
