package com.hpe.data;

import com.hpe.utils.Result;

/**
 * The type of the message.
 */
public enum MessageType {
    CALL, MSG;

    public static Result<MessageType> tryParse(String value) {
        try {
            return Result.ok(valueOf(value));
        } catch (IllegalArgumentException exception) {
            return Result.error(String.format("No <%s> value in the MessageType enum.", value));
        }
    }
}
