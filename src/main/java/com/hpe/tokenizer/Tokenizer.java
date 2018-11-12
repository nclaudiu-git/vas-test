package com.hpe.tokenizer;

import com.hpe.tokenizer.scanners.json.EndJsonObjectScanner;
import com.hpe.tokenizer.scanners.json.JsonScanner;
import com.hpe.tokenizer.scanners.json.KeyValuesScanner;
import com.hpe.tokenizer.scanners.json.StartJsonObjectScanner;
import com.hpe.data.Token;
import com.hpe.tokenizer.types.TokenListWithRemainingText;
import com.hpe.tokenizer.utils.Utils;
import com.hpe.utils.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * The tokenizer transforms a raw JSON string into a list of tokens skipping any whitespace between the tokens (but none inside values).
 * <li>It doesn't to any validation other that assuring that the JSON is well structured i.e. only valid JSON strings are tokenized.</li>
 * <li>As a result of running the process, it either returns a proper token list for a well formed JSON string or
 * an error with no previous tokens (though properly parsed until then) for invalid JSONs.</li>
 *
 * <p>
 * E.g. it turns <code>{"message_type": "CALL","timestamp": 1517645740,...}</code> into <code>["{" "message_type" ":" "CALL" "," "timestamp" ":" 1517645740 "," ... "}"]</code>
 *
 * <p>
 * Assumptions:
 * <li>No nested types</li>
 * <li>No unescaped strings i.e. no "Hello \"World!\""</li>
 * <li>Only strings or integer numbers for values</li>
 */
public class Tokenizer {

    public static Result<List<Token>> run(String text) {
        try {
            return compose(text,
                    StartJsonObjectScanner.class,
                    KeyValuesScanner.class,
                    EndJsonObjectScanner.class);
        } catch (InstantiationException | IllegalAccessException exception) {
            exception.printStackTrace();
            return Result.error("Unknown error. Possibly programmer error.");
        }
    }

    @SafeVarargs
    private static Result<List<Token>> compose(String text, Class<? extends JsonScanner>... classes) throws IllegalAccessException, InstantiationException {
        List<Token> tokens = new ArrayList<>();
        String remainingText = text;
        for (Class<? extends JsonScanner> clazz : classes) {
            Result<TokenListWithRemainingText> result = clazz.newInstance().run(remainingText);
            if (result.isError()) {
                return Result.error(result.getError());
            }

            remainingText = result.getValue().getRemainingText();
            tokens.addAll(result.getValue().getTokenList());
        }

        remainingText = Utils.skipWhitespaces(remainingText);
        if (!remainingText.isEmpty()) {
            return Result.error("Invalid extra data after closing JSON object.");
        }
        return Result.ok(tokens);
    }
}