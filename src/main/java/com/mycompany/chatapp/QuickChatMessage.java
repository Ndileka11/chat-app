package com.mycompany.chatapp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * A QuickChat message: validation, hash creation, sending, storing (JSON).
 */
public class QuickChatMessage {

    public static final String CELL_SUCCESS = "Cell phone number successfully captured.";
    public static final String CELL_FAILURE = "Cell phone number is incorrectly formatted or does not "
            + "contain an international code. Please correct the number and try again.";
    public static final String LENGTH_OK = "Message ready to send.";

    private static final int MAX_MESSAGE_LENGTH = 250;
    // "+" then a country code (1-3 digits) then the number (1-10 digits).
    // Replace with your Part 1 pattern if yours is different.
    private static final String CELL_PATTERN = "^\\+\\d{1,3}\\d{1,10}$";

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final List<QuickChatMessage> SENT_MESSAGES = new ArrayList<>();
    private static Path storageFile = Paths.get("storedMessages.json");

    private final String messageId;
    private final int messageNumber;
    private final String recipient;
    private final String text;
    private final String messageHash;

    /** Normal use: the message ID is generated randomly. */
    public QuickChatMessage(int messageNumber, String recipient, String text) {
        this(generateMessageId(), messageNumber, recipient, text);
    }

    /** Lets a test supply a fixed ID so the hash can be predicted. */
    public QuickChatMessage(String messageId, int messageNumber, String recipient, String text) {
        this.messageId = messageId;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.text = text;
        this.messageHash = createMessageHash();
    }

    // ---------- Validation ----------

    private static String generateMessageId() {
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            id.append(RANDOM.nextInt(10));
        }
        return id.toString();
    }

    /** The message ID must be 1-10 digits (never more than ten characters). */
    public boolean checkMessageID() {
        return messageId != null && messageId.matches("\\d{1,10}");
    }

    public static String checkRecipientCell(String cellNumber) {
        if (cellNumber != null && cellNumber.matches(CELL_PATTERN)) {
            return CELL_SUCCESS;
        }
        return CELL_FAILURE;
    }

    public String checkRecipientCell() {
        return checkRecipientCell(recipient);
    }

    public static String checkMessageLength(String messageText) {
        int length = messageText == null ? 0 : messageText.length();
        if (length <= MAX_MESSAGE_LENGTH) {
            return LENGTH_OK;
        }
        return "Message exceeds 250 characters by " + (length - MAX_MESSAGE_LENGTH)
                + "; please reduce the size.";
    }

    // ---------- Hash ----------

    /** First two digits of the ID : message number : FIRSTWORD + LASTWORD, in capitals. */
    public String createMessageHash() {
        String idStart = messageId.length() >= 2 ? messageId.substring(0, 2) : messageId;
        String trimmed = text == null ? "" : text.trim();
        String[] words = trimmed.split("\\s+");
        String first = cleanWord(words[0]);
        String last = cleanWord(words[words.length - 1]);
        return (idStart + ":" + messageNumber + ":" + first + last).toUpperCase();
    }

    private static String cleanWord(String word) {
        return word.replaceAll("[^A-Za-z0-9]", "");
    }

    // ---------- Send / discard / store ----------

    /** choice: 1 = send, 2 = disregard, 3 = store to send later. */
    public String sentMessage(int choice) {
        switch (choice) {
            case 1:
                SENT_MESSAGES.add(this);
                return "Message successfully sent.";
            case 2:
                return "Press 0 to delete the message.";
            case 3:
                storeMessage();
                return "Message successfully stored.";
            default:
                return "Invalid option. Choose 1 (send), 2 (disregard) or 3 (store).";
        }
    }

    /** Adds this message to the JSON file (a JSON array of message objects). */
    public void storeMessage() {
        String entry = toJson();
        try {
            String existing = Files.exists(storageFile)
                    ? Files.readString(storageFile, StandardCharsets.UTF_8).trim() : "";
            String content;
            if (existing.isEmpty() || existing.equals("[]") || !existing.endsWith("]")) {
                content = "[\n" + entry + "\n]";
            } else {
                String withoutEnd = existing.substring(0, existing.length() - 1).stripTrailing();
                content = withoutEnd + ",\n" + entry + "\n]";
            }
            Files.writeString(storageFile, content, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Could not store the message: " + e.getMessage());
        }
    }

    private String toJson() {
        return "  {\n"
                + "    \"messageID\": \"" + escapeJson(messageId) + "\",\n"
                + "    \"messageHash\": \"" + escapeJson(messageHash) + "\",\n"
                + "    \"recipient\": \"" + escapeJson(recipient) + "\",\n"
                + "    \"message\": \"" + escapeJson(text) + "\"\n"
                + "  }";
    }

    private static String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
    }

    // ---------- Reports ----------

    /** Details in the order the brief asks for: ID, hash, recipient, message. */
    public String getDetails() {
        return "Message ID: " + messageId + "\n"
                + "Message Hash: " + messageHash + "\n"
                + "Recipient: " + recipient + "\n"
                + "Message: " + text;
    }

    public static String printMessages() {
        if (SENT_MESSAGES.isEmpty()) {
            return "No messages have been sent.";
        }
        StringBuilder all = new StringBuilder();
        for (QuickChatMessage m : SENT_MESSAGES) {
            all.append(m.getDetails()).append("\n\n");
        }
        return all.toString().trim();
    }

    public static int returnTotalMessages() {
        return SENT_MESSAGES.size();
    }

    // ---------- Helpers (used by the tests) ----------

    public static void resetSentMessages() {
        SENT_MESSAGES.clear();
    }

    public static void setStorageFile(Path file) {
        storageFile = file;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getMessageHash() {
        return messageHash;
    }
}