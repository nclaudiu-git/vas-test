package com.hpe.data;

import com.hpe.utils.Result;

/**
 * The status code of the call. Only for CALL (message_type).
 */
public enum StatusCode {
    OK, KO;

    public static Result<StatusCode> tryParse(String value) {
        try {
            return Result.ok(valueOf(value));
        } catch (IllegalArgumentException exception) {
            return Result.error(String.format("No <%s> value in the StatusCode enum.", value));
        }
    }
}
