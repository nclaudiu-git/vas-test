package com.hpe.data;

import com.hpe.utils.Result;

/** The status of the message. */
public enum MessageStatus {
    DELIVERED, SEEN;

    public static Result<MessageStatus> tryParse(String value) {
        try {
            return Result.ok(valueOf(value));
        } catch (IllegalArgumentException exception) {
            return Result.error(String.format("No <%s> value in the MessageStatus enum.", value));
        }
    }
}
