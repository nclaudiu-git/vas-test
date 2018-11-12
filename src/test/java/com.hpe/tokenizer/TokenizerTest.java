package com.hpe.tokenizer;

import com.hpe.data.Token;
import com.hpe.data.TokenType;
import com.hpe.utils.Result;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class TokenizerTest {

    @Test
    public void shouldTokenizeCorrectCallJson() {
        List<Token> expectedList = Arrays.asList(
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

        Result<List<Token>> result = Tokenizer.run("{\"message_type\": \"CALL\",\"timestamp\": 1517645700,\"origin\": 34969000001,\"destination\": 34969000101,\"duration\": 120,\"status_code\": \"OK\",\"status_description\": \"OK\"}");

        assertFalse(result.isError());
        assertThat(result.getValue(), is(expectedList));
    }

    @Test
    public void shouldTokenizeCorrectMsgJson() {
        List<Token> expectedList = Arrays.asList(
                new Token(TokenType.STRING, "{"),
                new Token(TokenType.STRING, "message_type"),
                new Token(TokenType.STRING, ":"),
                new Token(TokenType.STRING, "MSG"),
                new Token(TokenType.STRING, ","),
                new Token(TokenType.STRING, "timestamp"),
                new Token(TokenType.STRING, ":"),
                new Token(TokenType.INTEGER, "1517559300"),
                new Token(TokenType.STRING, ","),
                new Token(TokenType.STRING, "origin"),
                new Token(TokenType.STRING, ":"),
                new Token(TokenType.INTEGER, "34960000001"),
                new Token(TokenType.STRING, ","),
                new Token(TokenType.STRING, "destination"),
                new Token(TokenType.STRING, ":"),
                new Token(TokenType.INTEGER, "34960000101"),
                new Token(TokenType.STRING, ","),
                new Token(TokenType.STRING, "message_content"),
                new Token(TokenType.STRING, ":"),
                new Token(TokenType.STRING, "1. HELLO"),
                new Token(TokenType.STRING, ","),
                new Token(TokenType.STRING, "message_status"),
                new Token(TokenType.STRING, ":"),
                new Token(TokenType.STRING, "DELIVERED"),
                new Token(TokenType.STRING, "}"));

        Result<List<Token>> result = Tokenizer.run("{\"message_type\": \"MSG\",\"timestamp\": 1517559300,\"origin\": 34960000001,\"destination\": 34960000101,\"message_content\": \"1. HELLO\",\"message_status\": \"DELIVERED\"}");

        assertFalse(result.isError());
        assertThat(result.getValue(), is(expectedList));
    }

    @Test
    public void shouldTokenizeEmptyJson() {
        List<Token> expectedList = Arrays.asList(
                new Token(TokenType.STRING, "{"),
                new Token(TokenType.STRING, "}"));

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