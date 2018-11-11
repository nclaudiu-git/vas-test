package com.hpe.tokenizer;

import com.hpe.tokenizer.utils.Token;
import com.hpe.utils.Result;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class TokenizerTest {

    @Test
    public void shouldTokenizeCorrectCallJson() {
        List<Token> expectedList = Arrays.asList(
                new Token(Token.Type.STRING, "{"),
                new Token(Token.Type.STRING, "message_type"),
                new Token(Token.Type.STRING, ":"),
                new Token(Token.Type.STRING, "CALL"),
                new Token(Token.Type.STRING, ","),
                new Token(Token.Type.STRING, "timestamp"),
                new Token(Token.Type.STRING, ":"),
                new Token(Token.Type.INTEGER, "1517645700"),
                new Token(Token.Type.STRING, ","),
                new Token(Token.Type.STRING, "origin"),
                new Token(Token.Type.STRING, ":"),
                new Token(Token.Type.INTEGER, "34969000001"),
                new Token(Token.Type.STRING, ","),
                new Token(Token.Type.STRING, "destination"),
                new Token(Token.Type.STRING, ":"),
                new Token(Token.Type.INTEGER, "34969000101"),
                new Token(Token.Type.STRING, ","),
                new Token(Token.Type.STRING, "duration"),
                new Token(Token.Type.STRING, ":"),
                new Token(Token.Type.INTEGER, "120"),
                new Token(Token.Type.STRING, ","),
                new Token(Token.Type.STRING, "status_code"),
                new Token(Token.Type.STRING, ":"),
                new Token(Token.Type.STRING, "OK"),
                new Token(Token.Type.STRING, ","),
                new Token(Token.Type.STRING, "status_description"),
                new Token(Token.Type.STRING, ":"),
                new Token(Token.Type.STRING, "OK"),
                new Token(Token.Type.STRING, "}"));

        Result<List<Token>> result = Tokenizer.run("{\"message_type\": \"CALL\",\"timestamp\": 1517645700,\"origin\": 34969000001,\"destination\": 34969000101,\"duration\": 120,\"status_code\": \"OK\",\"status_description\": \"OK\"}");

        assertFalse(result.isError());
        assertThat(result.getValue(), is(expectedList));
    }

    @Test
    public void shouldTokenizeCorrectMsgJson() {
        List<Token> expectedList = Arrays.asList(
                new Token(Token.Type.STRING, "{"),
                new Token(Token.Type.STRING, "message_type"),
                new Token(Token.Type.STRING, ":"),
                new Token(Token.Type.STRING, "MSG"),
                new Token(Token.Type.STRING, ","),
                new Token(Token.Type.STRING, "timestamp"),
                new Token(Token.Type.STRING, ":"),
                new Token(Token.Type.INTEGER, "1517559300"),
                new Token(Token.Type.STRING, ","),
                new Token(Token.Type.STRING, "origin"),
                new Token(Token.Type.STRING, ":"),
                new Token(Token.Type.INTEGER, "34960000001"),
                new Token(Token.Type.STRING, ","),
                new Token(Token.Type.STRING, "destination"),
                new Token(Token.Type.STRING, ":"),
                new Token(Token.Type.INTEGER, "34960000101"),
                new Token(Token.Type.STRING, ","),
                new Token(Token.Type.STRING, "message_content"),
                new Token(Token.Type.STRING, ":"),
                new Token(Token.Type.STRING, "1. HELLO"),
                new Token(Token.Type.STRING, ","),
                new Token(Token.Type.STRING, "message_status"),
                new Token(Token.Type.STRING, ":"),
                new Token(Token.Type.STRING, "DELIVERED"),
                new Token(Token.Type.STRING, "}"));

        Result<List<Token>> result = Tokenizer.run("{\"message_type\": \"MSG\",\"timestamp\": 1517559300,\"origin\": 34960000001,\"destination\": 34960000101,\"message_content\": \"1. HELLO\",\"message_status\": \"DELIVERED\"}");

        assertFalse(result.isError());
        assertThat(result.getValue(), is(expectedList));
    }

    @Test
    public void shouldTokenizeEmptyJson() {
        List<Token> expectedList = Arrays.asList(
                new Token(Token.Type.STRING, "{"),
                new Token(Token.Type.STRING, "}"));

        Result<List<Token>> result = Tokenizer.run("{}");

        assertFalse(result.isError());
        assertThat(result.getValue(), is(expectedList));
    }

    @Test
    public void shouldFailForInvalidJson() {
        Result<List<Token>> result = Tokenizer.run("{\"message_type\": \"CALL\",\"timestamp\": 1517645700,\"origin\": 34969000001,\"destination\": 34969000101,\"duration\": 120,\"status_code\": OK\",\"status_description\": \"OK\"}");

        assertTrue(result.isError());
        assertThat(result.getError(), startsWith("Invalid JSON string"));
    }
}