package net.devlab722.guestbook.filter.backend;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FilterDefinition {
    public static final ImmutableMap<String, String> DEFAULT_BUZZ_WORDS =
            new ImmutableMap.Builder<String, String>()
                    .put("microservices", "beeeeep")
                    .put("microservice", "beeeeep")
                    .put("bières", "bières (au chtijug? merci Zenika!)")
                    .put("Rancher", "Rancher (ça rulez!)")
                    .build();

    @Getter
    private final String emptyMessageCounterMeasure;
    @Getter
    private ImmutableMap<String, String> buzzWords;

    public FilterDefinition(
            @Value("${filter.definition.filename:null}")
                    String filterDefinitionFilename,
            @Value("${filter.empty.message.feedback:moi j'aime pas les messages vides}")
                    String emptyMessageCounterMeasure
    ) {
        this.emptyMessageCounterMeasure = emptyMessageCounterMeasure;
        initBuzzWords(filterDefinitionFilename);

    }

    private void initBuzzWords(String filterDefinitionFilename) {
        if (filterDefinitionFilename == null || filterDefinitionFilename.isEmpty()) {
            log.info("Using the default buzz words list because prop <filter.definition.filename> is not set");
            buzzWords = DEFAULT_BUZZ_WORDS;
        } else {
            File filterDefinitionFile = new File(filterDefinitionFilename);
            log.info("trying to load the buzz words list from file <" + filterDefinitionFilename + ">");
            Properties filterDefinitionAsProp = new Properties();
//            InputStream is = getClass().getClassLoader().getResourceAsStream(filterDefinitionFilename);
            try (InputStream is = new FileInputStream(filterDefinitionFile)) {
                try {
                    filterDefinitionAsProp.load(is);
                    buzzWords = Maps.fromProperties(filterDefinitionAsProp);
                    log.info("You're a boss, I found file <{}> and could load it!", filterDefinitionFilename);
                } catch (IOException e) {
                    log.info("Using the default buzz words list because file <{}>" +
                                    " cannot be parsed as a Property file (Exception was [{}])",
                            filterDefinitionFilename,
                            e.getMessage());
                    buzzWords = DEFAULT_BUZZ_WORDS;
                }
            } catch (FileNotFoundException e) {
                log.info("Using the default buzz words list because file <{}> cannot be found",
                        filterDefinitionFilename);
                buzzWords = DEFAULT_BUZZ_WORDS;
            } catch (IOException e) {
                log.info("Using the default buzz words list because IOException with message <{}> got caught while reading file <{}>",
                        e.getMessage(),
                        filterDefinitionFilename);
                buzzWords = DEFAULT_BUZZ_WORDS;
            }
        }
    }
}
