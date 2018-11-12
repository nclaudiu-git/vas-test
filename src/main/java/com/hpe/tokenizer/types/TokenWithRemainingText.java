package com.hpe.tokenizer.types;

import com.hpe.data.Token;

public class TokenWithRemainingText {
    private Token token;
    private String remainingText;

    public TokenWithRemainingText(Token token, String remainingText) {
        this.token = token;
        this.remainingText = remainingText;
    }

    public Token getToken() {
        return token;
    }

    public String getRemainingText() {
        return remainingText;
    }
}