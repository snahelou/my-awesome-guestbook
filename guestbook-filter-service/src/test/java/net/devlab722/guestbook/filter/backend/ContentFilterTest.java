package net.devlab722.guestbook.filter.backend;


import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ContentFilterTest {

    @DataProvider(name = "dataProviderFor_testFilterIfNeeded")
    public Object[][] dataProviderFor_testFilterIfNeeded() {
        FilterDefinition filterDefinition = new FilterDefinition(null, "mouarf");
        ContentFilter contentFilter = new ContentFilter(filterDefinition);
        return new Object[][]{
                {"vive les microservices",
                        "vive les beeeeep", true},
                {"trop fort mon microservice",
                        "trop fort mon beeeeep", true},
                {"microservices? vous avez dit microservices?",
                        "beeeeep? vous avez dit beeeeep?", true},
                {"Rancher l'orchestrateur docker",
                        "Rancher (ça rulez!) l'orchestrateur docker", true},
                {"des bières pour tout le monde!",
                        "des bières (au chtijug? merci Zenika!) pour tout le monde!", true},
                {"des bières pour tous ceux qui aiment les microservices!",
                        "des bières (au chtijug? merci Zenika!) pour tous ceux qui aiment les beeeeep!", true},
                {"vive les micro services",
                        "vive les micro services", false},
                {"",
                        filterDefinition.getEmptyMessageCounterMeasure(), true}
        };
    }

    @Test(dataProvider = "dataProviderFor_testFilterIfNeeded")
    public void testFilterIfNeeded(String input, String expectedContentValue, boolean expectedFilteredValue) {
        ContentFilter.FilteredMessage filteredMessage = new ContentFilter(new FilterDefinition(null, "mouarf")).filterIfNeeded(input);
        assertThat(filteredMessage.getContent()).isEqualTo(expectedContentValue);
        assertThat(filteredMessage.isFiltered()).isEqualTo(expectedFilteredValue);
    }

    @Test
    public void testFilterIfNeededWithNonDefaultBuzzWords() {
        FilterDefinition filterDefinition =
                new FilterDefinition("src/test/resources/filterDefinition.properties", "mouarf");
        ContentFilter.FilteredMessage filteredMessage =
                new ContentFilter(filterDefinition)
                        .filterIfNeeded("vive les microservices");
        assertThat(filteredMessage.getContent()).isEqualTo("vive les beeeeep bouia!");
        assertThat(filteredMessage.isFiltered()).isEqualTo(true);
    }
}