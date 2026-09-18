package com.mycompany.chatapp;

import com.mycompany.chatapp.model.Message;
import com.mycompany.chatapp.model.MessageStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    private Message message;

    @BeforeEach
    void setUp() {
        message = new Message("alice", "bob", "Hey, how's it going?");
    }

    @Test
    void newMessage_startsInPendingStatus() {
        assertEquals(MessageStatus.PENDING, message.getStatus());
    }

    @Test
    void newMessage_storesPayloadAndParticipants() {
        assertEquals("Hey, how's it going?", message.getPayload());
        assertEquals("alice", message.getSenderId());
        assertEquals("bob", message.getReceiverId());
        assertNotNull(message.getId());
        assertNotNull(message.getCreatedAt());
    }

    @Test
    void blankPayload_throwsException() {
        assertThrows(IllegalArgumentException.class,
            () -> new Message("alice", "bob", "   "));
    }

    @Test
    void nullPayload_throwsException() {
        assertThrows(IllegalArgumentException.class,
            () -> new Message("alice", "bob", null));
    }

    @Test
    void nullSenderId_throwsException() {
        assertThrows(NullPointerException.class,
            () -> new Message(null, "bob", "hi"));
    }

    @Test
    void markSent_transitionsFromPendingToSent() {
        message.markSent();
        assertEquals(MessageStatus.SENT, message.getStatus());
        assertNotNull(message.getSentAt());
    }

    @Test
    void markReceived_transitionsFromSentToReceived() {
        message.markSent();
        message.markReceived();
        assertEquals(MessageStatus.RECEIVED, message.getStatus());
        assertNotNull(message.getReceivedAt());
    }

    @Test
    void markRead_transitionsFromReceivedToRead() {
        message.markSent();
        message.markReceived();
        message.markRead();
        assertEquals(MessageStatus.READ, message.getStatus());
        assertNotNull(message.getReadAt());
    }

    @Test
    void fullLifecycle_timestampsAreInOrder() {
        message.markSent();
        message.markReceived();
        message.markRead();

        assertTrue(!message.getSentAt().isAfter(message.getReceivedAt()));
        assertTrue(!message.getReceivedAt().isAfter(message.getReadAt()));
        assertTrue(!message.getCreatedAt().isAfter(message.getSentAt()));
    }

    @Test
    void markReceived_beforeSent_throwsException() {
        assertThrows(IllegalStateException.class, () -> message.markReceived());
    }

    @Test
    void markRead_beforeReceived_throwsException() {
        message.markSent();
        assertThrows(IllegalStateException.class, () -> message.markRead());
    }

    @Test
    void markSent_twice_throwsException() {
        message.markSent();
        assertThrows(IllegalStateException.class, () -> message.markSent());
    }

    @Test
    void markRead_beforeSent_throwsException() {
        assertThrows(IllegalStateException.class, () -> message.markRead());
    }
}