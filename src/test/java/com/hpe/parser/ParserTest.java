package com.hpe.parser;

import com.hpe.data.*;
import com.hpe.utils.Result;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ParserTest {

    @Test
    public void shouldParseValidCallTokenList() {
        List<Token> tokens = Arrays.asList(
                new Token(TokenType.STRING, "{"),
                new Token(TokenType.STRING, "message_type"),
                new Token(TokenType.STRING, ":"),
                new Token(TokenType.STRING, "CALL"),
                new Token(TokenType.STRING, ","),
                new Token(TokenType.STRING, "timestamp"),
                new Token(TokenType.STRING, ":"),
                new Token(TokenType.INTEGER, "1517645700"),
                new Token(TokenType.STRING, ","),
                new Token(TokenType.STRING, "origin"),
                new Token(TokenType.STRING, ":"),
                new Token(TokenType.INTEGER, "34969000001"),
                new Token(TokenType.STRING, ","),
                new Token(TokenType.STRING, "destination"),
                new Token(TokenType.STRING, ":"),
                new Token(TokenType.INTEGER, "34969000101"),
                new Token(TokenType.STRING, ","),
                new Token(TokenType.STRING, "duration"),
                new Token(TokenType.STRING, ":"),
                new Token(TokenType.INTEGER, "120"),
                new Token(TokenType.STRING, ","),
                new Token(TokenType.STRING, "status_code"),
                new Token(TokenType.STRING, ":"),
                new Token(TokenType.STRING, "OK"),
                new Token(TokenType.STRING, ","),
                new Token(TokenType.STRING, "status_description"),
                new Token(TokenType.STRING, ":"),
                new Token(TokenType.STRING, "OK"),
                new Token(TokenType.STRING, "}"));

        List<KeyValue> expectedKeyValues = Arrays.asList(
                new KeyValue<>(Field.MESSAGE_TYPE, Result.ok(MessageType.CALL)),
                new KeyValue<>(Field.TIMESTAMP, Result.ok(1517645700L)),
                new KeyValue<>(Field.ORIGIN, Result.ok(34969000001L)),
                new KeyValue<>(Field.DESTINATION, Result.ok(34969000101L)),
                new KeyValue<>(Field.DURATION, Result.ok(120L)),
                new KeyValue<>(Field.STATUS_CODE, Result.ok(StatusCode.OK)),
                new KeyValue<>(Field.STATUS_DESCRIPTION, Result.ok("OK")));

        List<KeyValue> result = Parser.run(tokens);

        assertThat(result, is(expectedKeyValues));
    }

    @Test
    public void shouldParseValidMsgTokenList() {
        List<Token> tokens = Arrays.asList(
                new Token(TokenType.STRING, "{"),
                new Token(TokenType.STRING, "message_type"),
                new Token(TokenType.STRING, ":"),
                new Token(TokenType.STRING, "MSG"),
                new Token(TokenType.STRING, ","),
                new Token(TokenType.STRING, "timestamp"),
                new Token(TokenType.STRING, ":"),
                new Token(TokenType.INTEGER, "1517559360"),
                new Token(TokenType.STRING, ","),
                new Token(TokenType.STRING, "origin"),
                new Token(TokenType.STRING, ":"),
                new Token(TokenType.INTEGER, "447005963437"),
                new Token(TokenType.STRING, ","),
                new Token(TokenType.STRING, "destination"),
                new Token(TokenType.STRING, ":"),
                new Token(TokenType.INTEGER, "447005963734"),
                new Token(TokenType.STRING, ","),
                new Token(TokenType.STRING, "message_content"),
                new Token(TokenType.STRING, ":"),
                new Token(TokenType.STRING, "1. HELLO"),
                new Token(TokenType.STRING, ","),
                new Token(TokenType.STRING, "message_status"),
                new Token(TokenType.STRING, ":"),
                new Token(TokenType.STRING, "SEEN"),
                new Token(TokenType.STRING, "}"));
        List<KeyValue> expectedKeyValues = Arrays.asList(
                new KeyValue<>(Field.MESSAGE_TYPE, Result.ok(MessageType.MSG)),
                new KeyValue<>(Field.TIMESTAMP, Result.ok(1517559360L)),
                new KeyValue<>(Field.ORIGIN, Result.ok(447005963437L)),
                new KeyValue<>(Field.DESTINATION, Result.ok(447005963734L)),
                new KeyValue<>(Field.MESSAGE_CONTENT, Result.ok("1. HELLO")),
                new KeyValue<>(Field.MESSAGE_STATUS, Result.ok(MessageStatus.SEEN)));

        List<KeyValue> result = Parser.run(tokens);

        assertThat(result, is(expectedKeyValues));
    }

    @Test
    public void shouldParseEmptyMessageTypeToken() {
        List<Token> tokens = Arrays.asList(
                new Token(TokenType.STRING, "{"),
                new Token(TokenType.STRING, "message_type"),
                new Token(TokenType.STRING, ":"),
                new Token(TokenType.STRING, ""),
                new Token(TokenType.STRING, "}"));
        List<KeyValue> expectedKeyValues = Collections.singletonList(
                new KeyValue<>(Field.MESSAGE_TYPE, Result.error("No <> value in the MessageType enum.")));

        List<KeyValue> result = Parser.run(tokens);

        assertThat(result, is(expectedKeyValues));
    }

    @Test
    public void shouldParseInvalidTimestampTypeToken() {
        List<Token> tokens = Arrays.asList(
                new Token(TokenType.STRING, "{"),
                new Token(TokenType.STRING, "message_type"),
                new Token(TokenType.STRING, ":"),
                new Token(TokenType.STRING, "CALL"),
                new Token(TokenType.STRING, ","),
                new Token(TokenType.STRING, "timestamp"),
                new Token(TokenType.STRING, ":"),
                new Token(TokenType.STRING, "abc"), // STRING not INTEGER
                new Token(TokenType.STRING, "}"));
        List<KeyValue> expectedKeyValues = Arrays.asList(
                new KeyValue<>(Field.MESSAGE_TYPE, Result.ok(MessageType.CALL)),
                new KeyValue<>(Field.TIMESTAMP, Result.error("Invalid long value. Expected <abc> to be a long.")));

        List<KeyValue> result = Parser.run(tokens);

        assertThat(result, is(expectedKeyValues));
    }

    @Test
    public void shouldParseInvalidOriginTypeToken() {
        List<Token> tokens = Arrays.asList(
                new Token(TokenType.STRING, "{"),
                new Token(TokenType.STRING, "origin"),
                new Token(TokenType.STRING, ":"),
                new Token(TokenType.STRING, ""), // STRING not INTEGER
                new Token(TokenType.STRING, "}"));
        List<KeyValue> expectedKeyValues = Collections.singletonList(
                new KeyValue<>(Field.ORIGIN, Result.error("Invalid long value. Expected <> to be a long.")));

        List<KeyValue> result = Parser.run(tokens);

        assertThat(result, is(expectedKeyValues));
    }

    @Test
    public void shouldParseInvalidDestinationTypeToken() {
        List<Token> tokens = Arrays.asList(
                new Token(TokenType.STRING, "{"),
                new Token(TokenType.STRING, "destination"),
                new Token(TokenType.STRING, ":"),
                new Token(TokenType.STRING, ""), // STRING not INTEGER
                new Token(TokenType.STRING, "}"));
        List<KeyValue> expectedKeyValues = Collections.singletonList(
                new KeyValue<>(Field.DESTINATION, Result.error("Invalid long value. Expected <> to be a long.")));

        List<KeyValue> result = Parser.run(tokens);

        assertThat(result, is(expectedKeyValues));
    }

    @Test
    public void shouldParseInvalidStatusCodeToken() {
        List<Token> tokens = Arrays.asList(
                new Token(TokenType.STRING, "{"),
                new Token(TokenType.STRING, "status_code"),
                new Token(TokenType.STRING, ":"),
                new Token(TokenType.STRING, "K.O."),
                new Token(TokenType.STRING, "}"));
        List<KeyValue> expectedKeyValues = Collections.singletonList(
                new KeyValue<>(Field.STATUS_CODE, Result.error("No <K.O.> value in the StatusCode enum.")));

        List<KeyValue> result = Parser.run(tokens);

        assertThat(result, is(expectedKeyValues));
    }

    @Test
    public void shouldParseInvalidMessageStatusToken() {
        List<Token> tokens = Arrays.asList(
                new Token(TokenType.STRING, "{"),
                new Token(TokenType.STRING, "message_status"),
                new Token(TokenType.STRING, ":"),
                new Token(TokenType.STRING, "DELI"),
                new Token(TokenType.STRING, "}"));
        List<KeyValue> expectedKeyValues = Collections.singletonList(
                new KeyValue<>(Field.MESSAGE_STATUS, Result.error("No <DELI> value in the MessageStatus enum.")));

        List<KeyValue> result = Parser.run(tokens);

        assertThat(result, is(expectedKeyValues));
    }

    @Test
    public void shouldParseExtraTokenList() {
        List<Token> tokens = Arrays.asList(
                new Token(TokenType.STRING, "{"),
                new Token(TokenType.STRING, "message_type"),
                new Token(TokenType.STRING, ":"),
                new Token(TokenType.STRING, "CALL"),
                new Token(TokenType.STRING, ","),
                new Token(TokenType.STRING, "timestamp"),
                new Token(TokenType.STRING, ":"),
                new Token(TokenType.INTEGER, "1517645700"),
                new Token(TokenType.STRING, ","),
                new Token(TokenType.STRING, "extra"),
                new Token(TokenType.STRING, ":"),
                new Token(TokenType.STRING, "additional"),
                new Token(TokenType.STRING, "}"));

        List<KeyValue> expectedKeyValues = Arrays.asList(
                new KeyValue<>(Field.MESSAGE_TYPE, Result.ok(MessageType.CALL)),
                new KeyValue<>(Field.TIMESTAMP, Result.ok(1517645700L)),
                new KeyValue<>(Field.UNKNOWN, Result.ok("additional")));

        List<KeyValue> result = Parser.run(tokens);

        assertThat(result, is(expectedKeyValues));
    }
}