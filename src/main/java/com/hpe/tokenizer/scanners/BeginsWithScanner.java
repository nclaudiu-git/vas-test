package com.hpe.tokenizer.scanners;

import com.hpe.tokenizer.utils.Token;
import com.hpe.tokenizer.utils.TokenWithRemainingText;
import com.hpe.tokenizer.utils.Utils;
import com.hpe.utils.Result;

public class BeginsWithScanner {
    public static Result<TokenWithRemainingText> run(String text, String prefix) {
        String remainingText = Utils.skipWhitespaces(text);

        if (remainingText.startsWith(prefix)) {
            Token token = new Token(Token.Type.STRING, prefix);
            remainingText = remainingText.substring(prefix.length());
            return Result.ok(new TokenWithRemainingText(token, remainingText));
        }
        return Result.error(String.format("Missing beginning text <%s> in <%s> text", prefix, text));
    }
}
