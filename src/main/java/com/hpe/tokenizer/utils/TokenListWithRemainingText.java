package com.hpe.tokenizer.utils;

import java.util.List;

public class TokenListWithRemainingText {
    private List<Token> tokenList;
    private String remainingText;

    public TokenListWithRemainingText(List<Token> tokenList, String remainingText) {
        this.tokenList = tokenList;
        this.remainingText = remainingText;
    }

    public List<Token> getTokenList() {
        return tokenList;
    }

    public String getRemainingText() {
        return remainingText;
    }
}