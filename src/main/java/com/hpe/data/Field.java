package com.hpe.data;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * One of the fields in the JSON.
 */
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

    public static Set<Field> ofMessageType(MessageType messageType) {
        return Arrays.stream(values())
                .filter(field -> field.messageTypes.contains(messageType))
                .collect(Collectors.toSet());
    }

    public String getName() {
        return name;
    }
}
