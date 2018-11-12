package com.hpe.tokenizer.scanners.json;

import com.hpe.data.Token;
import com.hpe.data.TokenType;
import com.hpe.tokenizer.scanners.BeginsWithScanner;
import com.hpe.tokenizer.types.TokenListWithRemainingText;
import com.hpe.tokenizer.types.TokenWithRemainingText;
import com.hpe.utils.Result;

import java.util.Collections;

public class EndJsonObjectScanner extends JsonScanner {
    private static final String JSON_END_OBJECT = "}";

    @Override
    public Result<TokenListWithRemainingText> run(String text) {
        Result<TokenWithRemainingText> result = BeginsWithScanner.run(text, JSON_END_OBJECT);
        if (result.isError()) {
            return Result.error(result.getError());
        }

        Token token = new Token(TokenType.STRING, JSON_END_OBJECT);
        return Result.ok(new TokenListWithRemainingText(Collections.singletonList(token), result.getValue().getRemainingText()));
    }
}
