package com.mycompany.chatapp.model;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a single chat message with its payload and delivery lifecycle.
 *
 * Status transitions only move forward:
 * PENDING -> SENT -> RECEIVED -> READ
 */
public class Message {

    private final String id;
    private final String senderId;
    private final String receiverId;
    private final String payload;
    private final Instant createdAt;

    private MessageStatus status;
    private Instant sentAt;
    private Instant receivedAt;
    private Instant readAt;

    public Message(String senderId, String receiverId, String payload) {
        if (payload == null || payload.isBlank()) {
            throw new IllegalArgumentException("Message payload cannot be null or blank");
        }
        this.id = UUID.randomUUID().toString();
        this.senderId = Objects.requireNonNull(senderId, "senderId cannot be null");
        this.receiverId = Objects.requireNonNull(receiverId, "receiverId cannot be null");
        this.payload = payload;
        this.createdAt = Instant.now();
        this.status = MessageStatus.PENDING;
    }

    public void markSent() {
        requireStatus(MessageStatus.PENDING, "sent");
        this.status = MessageStatus.SENT;
        this.sentAt = Instant.now();
    }

    public void markReceived() {
        requireStatus(MessageStatus.SENT, "received");
        this.status = MessageStatus.RECEIVED;
        this.receivedAt = Instant.now();
    }

    public void markRead() {
        requireStatus(MessageStatus.RECEIVED, "read");
        this.status = MessageStatus.READ;
        this.readAt = Instant.now();
    }

    private void requireStatus(MessageStatus required, String actionName) {
        if (this.status != required) {
            throw new IllegalStateException(
                "Cannot mark message as " + actionName + " from status " + this.status
                    + " (expected " + required + ")");
        }
    }

    public String getId() { return id; }
    public String getSenderId() { return senderId; }
    public String getReceiverId() { return receiverId; }
    public String getPayload() { return payload; }
    public MessageStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getSentAt() { return sentAt; }
    public Instant getReceivedAt() { return receivedAt; }
    public Instant getReadAt() { return readAt; }

    @Override
    public String toString() {
        return "Message{id='" + id + "', senderId='" + senderId +
            "', receiverId='" + receiverId + "', payload='" + payload +
            "', status=" + status + '}';
    }
}