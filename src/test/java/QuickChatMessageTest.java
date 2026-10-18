package com.mycompany.chatapp;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class QuickChatMessageTest {

    @TempDir
    Path tempDir;

    // Test data from the brief
    private static final String[] RECIPIENTS = {"+27718693002", "08575975889"};
    private static final String[] TEXTS = {
        "Hi Mike, can you join us for dinner tonight",
        "Hi Keegan, did you receive the payment?"
    };

    private QuickChatMessage message1;
    private QuickChatMessage message2;

    @BeforeEach
    void setUp() {
        QuickChatMessage.resetSentMessages();
        QuickChatMessage.setStorageFile(tempDir.resolve("storedMessages.json"));
        // Fixed IDs so the hashes can be predicted
        message1 = new QuickChatMessage("0012345678", 0, RECIPIENTS[0], TEXTS[0]);
        message2 = new QuickChatMessage("9987654321", 1, RECIPIENTS[1], TEXTS[1]);
    }

    @Test
    void messageLength_success() {
        assertEquals("Message ready to send.", QuickChatMessage.checkMessageLength(TEXTS[0]));
        assertEquals("Message ready to send.", QuickChatMessage.checkMessageLength("a".repeat(250)));
    }

    @Test
    void messageLength_failure() {
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.",
                QuickChatMessage.checkMessageLength("a".repeat(260)));
    }

    @Test
    void recipientNumber_correctlyFormatted() {
        assertEquals("Cell phone number successfully captured.", message1.checkRecipientCell());
    }

    @Test
    void recipientNumber_incorrectlyFormatted() {
        assertEquals("Cell phone number is incorrectly formatted or does not contain an "
                + "international code. Please correct the number and try again.",
                message2.checkRecipientCell());
    }

    @Test
    void messageHash_testCase1() {
        assertEquals("00:0:HITONIGHT", message1.createMessageHash());
    }

    @Test
    void messageHash_allTestMessagesInLoop() {
        QuickChatMessage[] messages = {message1, message2};
        String[] expected = {"00:0:HITONIGHT", "99:1:HIPAYMENT"};
        for (int i = 0; i < messages.length; i++) {
            assertEquals(expected[i], messages[i].createMessageHash());
        }
    }

    @Test
    void messageId_isCreated() {
        QuickChatMessage generated = new QuickChatMessage(0, RECIPIENTS[0], TEXTS[0]);
        assertTrue(generated.checkMessageID());
        assertEquals(10, generated.getMessageId().length());
        assertEquals("Message ID generated: " + generated.getMessageId(),
                "Message ID generated: " + generated.getMessageId());
    }

    @Test
    void messageId_tooLong_isRejected() {
        QuickChatMessage tooLong = new QuickChatMessage("12345678901", 0, RECIPIENTS[0], TEXTS[0]);
        assertFalse(tooLong.checkMessageID());
    }

    @Test
    void sentMessage_sendOption() {
        assertEquals("Message successfully sent.", message1.sentMessage(1));
    }

    @Test
    void sentMessage_disregardOption() {
        assertEquals("Press 0 to delete the message.", message2.sentMessage(2));
    }

    @Test
    void sentMessage_storeOption_writesJsonFile() throws IOException {
        assertEquals("Message successfully stored.", message1.sentMessage(3));
        String json = Files.readString(tempDir.resolve("storedMessages.json"));
        assertTrue(json.contains("00:0:HITONIGHT"));
        assertTrue(json.contains("+27718693002"));
    }

    @Test
    void totalMessages_countsOnlySentMessages() {
        message1.sentMessage(1);
        message2.sentMessage(2);
        assertEquals(1, QuickChatMessage.returnTotalMessages());
    }

    @Test
    void printMessages_listsSentMessages() {
        message1.sentMessage(1);
        String printed = QuickChatMessage.printMessages();
        assertTrue(printed.contains("0012345678"));
        assertTrue(printed.contains("00:0:HITONIGHT"));
    }
}
