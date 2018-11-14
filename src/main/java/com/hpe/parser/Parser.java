package com.hpe.parser;

import com.hpe.data.*;
import com.hpe.parser.utils.Utils;
import com.hpe.utils.Result;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The parser takes a list of tokens (the result of the tokenization of a raw JSON) and returns a list of key-value pairs
 * while also validating that each field type has the proper value type i.e. string or integer (in the mathematical sense, whole number).
 * <p>
 * Precondition:
 * <li>The tokens list is balanced i.e. it starts with a "{" token and ends with a "}" and has a well-formed list of
 * key values tokens (one "key" token, followed by a ":" token, followed by a "value" one) while the key-value pairs
 * themselves are separated by the "," token.</li>
 */
public class Parser {
    private static final String JSON_KEY_VALUE_PAIRS_DELIMITER = ",";
    private static final String JSON_END_OBJECT = "}";

    public static List<KeyValue> run(List<Token> tokens) {
        List<KeyValue> keyValueList = new ArrayList<>();
        Iterator<Token> iterator = tokens.iterator();

        // skip starting bracket
        iterator.next();

        while (true) {
            Token token = iterator.next();
            if (token.getValue().equals(JSON_END_OBJECT)) {
                // end bracket
                break;
            } else if (token.getValue().equals(JSON_KEY_VALUE_PAIRS_DELIMITER)) {
                // skip current comma token and read next token which is the key
                token = iterator.next();
            }
            String key = token.getValue();

            // skip colon token
            iterator.next();

            // read the value
            token = iterator.next();
            keyValueList.add(decodeToken(key, token));
        }
        return keyValueList;
    }

    private static KeyValue decodeToken(String key, Token token) {
        if (Field.MESSAGE_TYPE.getName().equals(key)) {
            return new KeyValue<>(Field.MESSAGE_TYPE, MessageType.tryParse(token.getValue()));
        } else if (Field.TIMESTAMP.getName().equals(key)) {
            return new KeyValue<>(Field.TIMESTAMP, Utils.tryParseLong(token.getValue()));
        } else if (Field.ORIGIN.getName().equals(key)) {
            return new KeyValue<>(Field.ORIGIN, Utils.tryParseLong(token.getValue()));
        } else if (Field.DESTINATION.getName().equals(key)) {
            return new KeyValue<>(Field.DESTINATION, Utils.tryParseLong(token.getValue()));
        } else if (Field.DURATION.getName().equals(key)) {
            return new KeyValue<>(Field.DURATION, Utils.tryParseLong(token.getValue()));
        } else if (Field.STATUS_CODE.getName().equals(key)) {
            return new KeyValue<>(Field.STATUS_CODE, StatusCode.tryParse(token.getValue()));
        } else if (Field.STATUS_DESCRIPTION.getName().equals(key)) {
            return new KeyValue<>(Field.STATUS_DESCRIPTION, Utils.tryParseString(token));
        } else if (Field.MESSAGE_CONTENT.getName().equals(key)) {
            return new KeyValue<>(Field.MESSAGE_CONTENT, Utils.tryParseString(token));
        } else if (Field.MESSAGE_STATUS.getName().equals(key)) {
            return new KeyValue<>(Field.MESSAGE_STATUS, MessageStatus.tryParse(token.getValue()));
        }
        return new KeyValue<>(Field.UNKNOWN, Result.ok(token.getValue()));
    }
}
