package com.hpe.tokenizer.scanners;

import com.hpe.data.Token;
import com.hpe.data.TokenType;
import com.hpe.tokenizer.types.TokenWithRemainingText;
import com.hpe.tokenizer.utils.Utils;
import com.hpe.utils.Result;

public class IntegerScanner {

    public static Result<TokenWithRemainingText> run(String text) {
        String remainingText = Utils.skipWhitespaces(text);

        StringBuffer buffer = new StringBuffer();
        while (remainingText.length() > 0 && remainingText.charAt(0) >= '0' && remainingText.charAt(0) <= '9') {
            buffer.append(remainingText.charAt(0));
            remainingText = remainingText.substring(1);
        }

        if (buffer.length() > 0) {
            Token token = new Token(TokenType.INTEGER, buffer.toString());
            return Result.ok(new TokenWithRemainingText(token, remainingText));
        }
        return Result.error(String.format("Invalid JSON string. Expected <%s> to begin with (optional) whitespaces followed by an INTEGER but got a STRING instead", text));
    }
}
