package com.hpe.tokenizer.scanners.json;

import com.hpe.tokenizer.scanners.BeginsWithScanner;
import com.hpe.tokenizer.scanners.StringScanner;
import com.hpe.tokenizer.scanners.IntegerScanner;
import com.hpe.tokenizer.utils.Token;
import com.hpe.tokenizer.utils.TokenListWithRemainingText;
import com.hpe.tokenizer.utils.TokenWithRemainingText;
import com.hpe.utils.Result;

import java.util.ArrayList;
import java.util.List;

public class KeyValuesScanner extends JsonScanner {
    private static final String JSON_KEY_VALUE_DELIMITER = ":";
    private static final String JSON_KEY_VALUE_PAIRS_DELIMITER = ",";
    private static final String STRING_BEGIN_QUOTE = "\"";

    @Override
    public Result<TokenListWithRemainingText> run(String text) {
        // nothing to do for empty objects
        if (noKeyValueExists(text)) {
            return Result.ok(new TokenListWithRemainingText(new ArrayList<>(), text));
        }

        List<Token> accumulator = new ArrayList<>();
        Result<String> result = scanAndAccumulateKeyValues(text, accumulator);
        if (result.isError()) {
            return Result.error(result.getError());
        }
        return Result.ok(new TokenListWithRemainingText(accumulator, result.getValue()));
    }

    private boolean noKeyValueExists(String text) {
        Result<TokenListWithRemainingText> result = new EndJsonObjectScanner().run(text);
        return !result.isError();
    }

    private Result<String> scanAndAccumulateKeyValues(String text, List<Token> accumulator) {
        Result<TokenWithRemainingText> keyResult = StringScanner.run(text);
        if (keyResult.isError()) {
            return Result.error(keyResult.getError());
        }
        accumulator.add(keyResult.getValue().getToken());
        String remainingText = keyResult.getValue().getRemainingText();

        Result<TokenWithRemainingText> colonResult = BeginsWithScanner.run(remainingText, JSON_KEY_VALUE_DELIMITER);
        if (colonResult.isError()) {
            return Result.error(colonResult.getError());
        }
        accumulator.add(colonResult.getValue().getToken());
        remainingText = colonResult.getValue().getRemainingText();

        TokenWithRemainingText valueToken;
        // the next value might be either a string or an integer
        Result<TokenWithRemainingText> stringValueResult = BeginsWithScanner.run(remainingText, STRING_BEGIN_QUOTE);
        if (stringValueResult.isError()) {
            Result<TokenWithRemainingText> integerValueResult = IntegerScanner.run(remainingText);
            if (integerValueResult.isError()) {
                return Result.error(integerValueResult.getError());
            }
            valueToken = integerValueResult.getValue();
        } else {
            // the value is a string
            stringValueResult = StringScanner.run(remainingText);
            if (stringValueResult.isError()) {
                return Result.error(stringValueResult.getError());
            }
            valueToken = stringValueResult.getValue();
        }
        accumulator.add(valueToken.getToken());
        remainingText = valueToken.getRemainingText();

        Result<TokenWithRemainingText> keyValuePairDelimiterResult = BeginsWithScanner.run(remainingText, JSON_KEY_VALUE_PAIRS_DELIMITER);
        if (keyValuePairDelimiterResult.isError()) {
            // we must be at the end of the final pair
            return Result.ok(remainingText);
        }
        accumulator.add(keyValuePairDelimiterResult.getValue().getToken());
        remainingText = keyValuePairDelimiterResult.getValue().getRemainingText();

        return scanAndAccumulateKeyValues(remainingText, accumulator);
    }
}
