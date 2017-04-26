package net.devlab722.guestbook.filter.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Lists;

import net.devlab722.guestbook.filter.backend.FilterDefinition;
import net.devlab722.guestbook.filter.backend.FilterMatch;

@Controller
@RequestMapping(path = "/api/v1/filter/definition")
public class FilterDefinitionController {

    private final FilterDefinition filterDefinition;

    public FilterDefinitionController(@Autowired FilterDefinition filterDefinition) {
        this.filterDefinition = filterDefinition;
    }

    @GetMapping
    public ResponseEntity<List<FilterMatch>> getDefinitionElements() {
        List<FilterMatch> matches = Lists.newArrayList();
        filterDefinition.getBuzzWords()
                .forEach((k, v) -> matches.add(FilterMatch.builder().origin(k).replaced(v).build()));
        return new ResponseEntity<>(matches, HttpStatus.OK);
    }
}
