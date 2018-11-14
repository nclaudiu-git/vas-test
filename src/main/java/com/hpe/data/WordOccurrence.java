package com.hpe.data;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Wrapper for the word occurrences evaluation.
 */
public class WordOccurrence {
    private String word;
    private long occurrences;

    private WordOccurrence(String word, long occurrences) {
        this.word = word;
        this.occurrences = occurrences;
    }

    public String getWord() {
        return word;
    }

    public long getOccurrences() {
        return occurrences;
    }

    public static class Parser {
        private static final List<String> KEYWORDS = Arrays.asList("ARE", "YOU", "FINE", "HELLO", "NOT");

        /**
         * @return an unsorted list of word occurrences of the given keywords.
         */
        public static List<WordOccurrence> run(String text) {
            Map<String, Integer> map = new HashMap<String, Integer>() {
                {
                    KEYWORDS.stream().forEach(word -> put(word, 0));
                }
            };

            Pattern pattern = Pattern.compile("[\\w]+");
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                String word = text.substring(matcher.start(), matcher.end());
                if (KEYWORDS.contains(word)) {
                    map.put(word, map.get(word) + 1);
                }
            }

            return map.entrySet().stream().map(entry -> new WordOccurrence(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());
        }
    }
}
