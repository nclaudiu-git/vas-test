package com.hpe.services;

import com.hpe.Cache;
import com.hpe.TestConfig;
import com.hpe.data.Counters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class JsonServiceTest {

    @Autowired
    private JsonService service;

    @Autowired
    private Cache cache;

    private Path getPath(String resourceFile) {
        try {
            return Paths.get(getClass().getClassLoader().getResource(resourceFile).toURI());
        } catch (URISyntaxException exception) {
            throw new RuntimeException("Could not get path for resource file.");
        }
    }

    @Test
    public void shouldProcessJsonFileWithBothValidAndInvalidLines() throws IOException {
        try (Stream<String> lines = Files.lines(getPath("MCP_20180131.json"))) {
            service.process("20180131", lines);

            assertTrue(cache.keySet().equals(new HashSet<>(Collections.singletonList("20180131"))));

            Counters counters = cache.values().iterator().next();
            assertTrue(counters.getRowsWithMissingFieldsCounter() == 0);
            assertTrue(counters.getMessagesWithBlankContentCounter() == 4);
            assertTrue(counters.getRowsWithFieldsErrorsCounter() == 11);
            assertTrue(counters.getOriginCallsByCountryCode().keySet().equals(new HashSet<>(Arrays.asList(34L, 44L))));
            assertTrue(new ArrayList<>(counters.getOriginCallsByCountryCode().values()).equals(Arrays.asList(6, 6)));
            assertTrue(counters.getDestinationCallsByCountryCode().keySet().equals(new HashSet<>(Arrays.asList(34L, 44L))));
            assertTrue(new ArrayList<>(counters.getDestinationCallsByCountryCode().values()).equals(Arrays.asList(6, 6)));
            assertTrue(counters.getOkCallsCounter() == 13);
            assertTrue(counters.getKoCallsCounter() == 5);
            assertTrue(counters.getAverageCallsDurationByCountryCode().keySet().equals(new HashSet<>(Arrays.asList(34L, 44L))));
            assertTrue(new ArrayList<>(counters.getAverageCallsDurationByCountryCode().values()).equals(Arrays.asList(150.0f, 10.0f)));
            assertTrue(counters.getWordsOccurrenceRanking().equals(Arrays.asList("HELLO", "ARE", "FINE", "YOU", "NOT")));
            assertTrue(counters.getRowsCounter() == 48);
            assertTrue(counters.getCallsCounter() == 18);
            assertTrue(counters.getMessagesCounter() == 19);
            assertTrue(counters.getDistinctOriginCountryCodes().equals(new HashSet<>(Arrays.asList(34L, 44L))));
            assertTrue(counters.getDistinctDestinationCountryCodes().equals(new HashSet<>(Arrays.asList(34L, 44L))));
        }
    }
}
