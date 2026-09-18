package com.mycompany.chatapp.model;

/**
 * Represents the lifecycle state of a chat message.
 *
 * PENDING  -> message created locally, not yet sent to the server
 * SENT     -> message left the sender's client and reached the server
 * RECEIVED -> message was delivered to the recipient's device
 * READ     -> recipient has opened/viewed the message
 */
public enum MessageStatus {
    PENDING,
    SENT,
    RECEIVED,
    READ
}