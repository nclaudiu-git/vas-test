package com.hpe.data;

import java.util.Arrays;
import java.util.List;

/** One of the fields in the JSON. */
public enum Field {
    MESSAGE_TYPE("message_type", MessageType.CALL, MessageType.MSG),
    TIMESTAMP("timestamp", MessageType.CALL, MessageType.MSG),
    ORIGIN("origin", MessageType.CALL, MessageType.MSG),
    DESTINATION("destination", MessageType.CALL, MessageType.MSG),
    DURATION("duration", MessageType.CALL),
    STATUS_CODE("status_code", MessageType.CALL),
    STATUS_DESCRIPTION("status_description", MessageType.CALL),
    MESSAGE_CONTENT("message_content", MessageType.MSG),
    MESSAGE_STATUS("message_status", MessageType.MSG),
    UNKNOWN("unknown");

    private final String name;
    private final List<MessageType> messageTypes;

    Field(String name, MessageType... messageTypes) {
        this.name = name;
        this.messageTypes = Arrays.asList(messageTypes);
    }

    public String getName() {
        return name;
    }
}
