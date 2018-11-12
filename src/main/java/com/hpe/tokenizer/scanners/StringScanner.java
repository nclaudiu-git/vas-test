package com.hpe.tokenizer.scanners;

import com.hpe.data.Token;
import com.hpe.data.TokenType;
import com.hpe.tokenizer.types.TokenWithRemainingText;
import com.hpe.tokenizer.utils.Utils;
import com.hpe.utils.Result;

public class StringScanner {
    private static final String STRING_START_QUOTE = "\"";
    private static final String STRING_END_QUOTE = "\"";

    public static Result<TokenWithRemainingText> run(String text) {
        Result<TokenWithRemainingText> result = scanStartStringQuote(text);
        if (result.isError()) {
            return Result.error(String.format("Missing starting quote for string in <%s>", text));
        }

        return scanString(result.getValue().getRemainingText());
    }

    private static Result<TokenWithRemainingText> scanStartStringQuote(String text) {
        String remainingText = Utils.skipWhitespaces(text);
        return BeginsWithScanner.run(remainingText, STRING_START_QUOTE);
    }

    private static Result<TokenWithRemainingText> scanString(String text) {
        int index = text.indexOf(STRING_END_QUOTE);
        if (index == -1) {
            return Result.error(String.format("Missing ending quote for key in <%s>", text));
        }
        Token token = new Token(TokenType.STRING, text.substring(0, index));
        String remainingText = text.substring(index + 1);
        return Result.ok(new TokenWithRemainingText(token, remainingText));
    }
}
