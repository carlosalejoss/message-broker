package com.broker.core;

import java.io.Serializable;
import java.time.Instant;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String content;
    private final Instant timestamp;

    public Message(String content) {
        this.content = content;
        this.timestamp = Instant.now();
    }

    public String getContent() {
        return content;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}