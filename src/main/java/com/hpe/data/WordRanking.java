package com.hpe.data;

/**
 * Wrapper for the word ranking evaluation.
 */
public class WordRanking {
    private String word;
    private int ranking;

    public WordRanking(String word, int ranking) {
        this.word = word;
        this.ranking = ranking;
    }

    public String getWord() {
        return word;
    }

    public int getRanking() {
        return ranking;
    }
}
