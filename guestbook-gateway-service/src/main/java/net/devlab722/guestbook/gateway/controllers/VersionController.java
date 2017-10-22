package net.devlab722.guestbook.gateway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import net.devlab722.guestbook.api.Version;
import net.devlab722.guestbook.gateway.backend.StorageBackend;
import net.devlab722.guestbook.gateway.backend.FilterBackend;

@Slf4j
@Controller
@RequestMapping("/api/v1/version")
public class VersionController {

    @Value("${product.version:unknown}")
    public String productVersion;

    @Value("${gateway.version:unknown}")
    public String gatewayVersion;


    @Autowired
    FilterBackend filterBackend;

    @Autowired
    StorageBackend storageBackend;

    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    Version echoVersion() {

        Version.VersionBuilder versionBuilder = Version.builder()
                .product(productVersion)
                .gateway(gatewayVersion);

        filterBackend.rxFilterVersion()
                .map(rec -> {
                    if (rec.getStatusCode() != HttpStatus.OK) {
                        log.error("echoVersion(): while requesting filter version, expected status code <" +
                                HttpStatus.OK + "> from the filter service, got <" +
                                rec.getStatusCode() + ">");
                        return new ResponseEntity<>(
                                Version.builder()
                                        .filter("unknown (got statusCode " +
                                                rec.getStatusCode()+
                                                ")")
                                        .build(), HttpStatus.OK);
                    } else {
                        return rec;
                    }
                }).subscribe(
                rec -> versionBuilder.filter(rec.getBody().getFilter()),
                error -> versionBuilder.filter("unknown (" + error.getMessage() + ")")
        );

        storageBackend.rxStorageVersion()
                .map(rec -> {
                    if (rec.getStatusCode() != HttpStatus.OK) {
                        log.error("echoVersion(): while requesting storage version, expected status code <" +
                                HttpStatus.OK + "> from the storage service, got <" +
                                rec.getStatusCode() + ">");
                        return new ResponseEntity<>(
                                Version.builder()
                                        .storage("unknown (got statusCode " +
                                                rec.getStatusCode() +
                                                ")")
                                        .build(), HttpStatus.OK);
                    } else {
                        return rec;
                    }
                }).subscribe(
                rec -> versionBuilder.storage(rec.getBody().getStorage()),
                error -> versionBuilder.storage("unknown (" + error.getMessage() + ")")
        );


        return versionBuilder.build();
    }
}
