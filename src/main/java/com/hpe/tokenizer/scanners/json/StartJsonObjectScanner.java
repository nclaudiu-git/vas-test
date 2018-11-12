package com.hpe.tokenizer.scanners.json;

import com.hpe.data.TokenType;
import com.hpe.tokenizer.scanners.BeginsWithScanner;
import com.hpe.data.Token;
import com.hpe.tokenizer.types.TokenListWithRemainingText;
import com.hpe.tokenizer.types.TokenWithRemainingText;
import com.hpe.utils.Result;

import java.util.Collections;

public class StartJsonObjectScanner extends JsonScanner {
    private static final String JSON_START_OBJECT = "{";

    @Override
    public Result<TokenListWithRemainingText> run(String text) {
        Result<TokenWithRemainingText> result = BeginsWithScanner.run(text, JSON_START_OBJECT);
        if(result.isError()) {
            return Result.error(result.getError());
        }

        Token token = new Token(TokenType.STRING, JSON_START_OBJECT);
        return Result.ok(new TokenListWithRemainingText(Collections.singletonList(token), result.getValue().getRemainingText()));
    }
}
