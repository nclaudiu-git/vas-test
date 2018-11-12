package com.hpe.parser.utils;

import com.hpe.data.Token;
import com.hpe.data.TokenType;
import com.hpe.utils.Result;

public class Utils {
    public static Result<Long> tryParseLong(String value) {
        try {
            return Result.ok(Long.parseLong(value));
        } catch (NumberFormatException exception) {
            return Result.error(String.format("Invalid long value. Expected <%s> to be a long.", value));
        }
    }

    public static Result<String> tryParseString(Token token) {
        if (token.getType() == TokenType.STRING) {
            return Result.ok(token.getValue());
        }
        return Result.error(String.format("Expected token type to be <%s>. Got <%s> instead.", TokenType.STRING, token.getType()));
    }
}
