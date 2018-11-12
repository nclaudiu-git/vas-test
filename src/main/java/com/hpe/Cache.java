package com.hpe;

import com.hpe.data.*;
import com.hpe.parser.utils.Utils;
import com.hpe.data.Token;
import com.hpe.utils.Result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * The parser takes a list of tokens (the result of tokenization of a raw JSON) and returns a list of fields
 * while also validating that the each field type has the proper value type i.e. string or integer.
 * <p>
 * Preconditions:
 * <li>The tokens list is balanced i.e. it starts with a "{" token and ends with a "}" and has a well-formed list of
 * key values tokens (one "key" token, followed by a ":" token, followed by a "value" one) while the key-value pairs
 * themselves are separated by the "," token.</li>
 * <p>
 * Cache:
 * 20180201:
 * rowsCounter = 10
 * callsCounter = 5
 * messagesCounter = 5
 * distinctOriginCountryCodes = [15, 10, 20, ....]
 * processingTimeInMillis = 130
 * 20180202:
 * rowsCounter = 6
 * callsCounter = 4
 * messagesCounter = 0
 * distinctOriginCountryCodes = [15, 40, ....]
 * processingTimeInMillis = 50
 * <p>
 * Counters:
 * <p>
 * Metrics:
 * Number of rows with missing fields
 * Number of messages with blank content
 * Number of rows with fields errors
 * Number of calls origin/destination grouped by country code (https://en.wikipedia.org/wiki/MSISDN)
 * Relationship between OK/KO calls
 * Average call duration grouped by country code (https://en.wikipedia.org/wiki/MSISDN)
 * Word occurrence ranking for the given words in message_content field.
 * <p>
 * KPIs:
 * Total number of processed JSON files => count cache keys
 * Total number of rows => sum (each rowsCounter)
 * Total number of calls => sum (each callsCounter)
 * Total number of messages => sum (each messagesCounter)
 * Total number of different origin country codes => count distinct (union distinctOriginCountryCodes)
 * Total number of different destination country codes => count distinct (union distinctDestinationCountryCodes)
 * Duration of each JSON process => sum (each processingTimeInMillis)
 */
public class Cache {
    private static final String JSON_KEY_VALUE_PAIRS_DELIMITER = ",";
    private static final String JSON_END_OBJECT = "}";

    private static final List<String> CALL_FIELDS = Arrays.asList(
            "message_type",
            "timestamp",
            "origin",
            "destination",
            "duration",
            "status_code",
            "status_description"
    );

    private static final List<String> MSG_FIELDS = Arrays.asList(
            "message_type",
            "timestamp",
            "origin",
            "destination",
            "message_content",
            "message_status"
    );

    private static final List<String> RANKING_WORDS = Arrays.asList("ARE", "YOU", "FINE", "HELLO", "NOT");

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
