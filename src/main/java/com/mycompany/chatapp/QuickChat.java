package com.mycompany.chatapp;

import java.util.Scanner;

/**
 * QuickChat console application (Part 2). Console only - no GUI.
 */
public class QuickChat {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        if (!loginSucceeded(scanner)) {
            System.out.println("Login failed. You cannot send messages.");
            return;
        }

        System.out.println("Welcome to QuickChat.");
        int limit = readPositiveInt(scanner, "How many messages do you wish to enter? ");
        int entered = 0;
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    if (entered >= limit) {
                        System.out.println("You have already entered all " + limit + " messages.");
                    } else {
                        handleNewMessage(scanner, entered);
                        entered++;
                        if (entered == limit) {
                            System.out.println("All messages entered. Total messages sent: "
                                    + QuickChatMessage.returnTotalMessages());
                        }
                    }
                    break;
                case "2":
                    System.out.println("Coming Soon.");
                    break;
                case "3":
                    running = false;
                    break;
                default:
                    System.out.println("Please enter 1, 2 or 3.");
            }
        }

        System.out.println("Total messages sent: " + QuickChatMessage.returnTotalMessages());
        System.out.println("Goodbye!");
    }

    /**
     * PART 1 LOGIN GOES HERE.
     * Ask for the username and password and call your Part 1 methods
     * (registerUser / loginUser / returnLoginStatus). Return true only when
     * the login is successful.
     * It returns true for now only so you can test the menu - replace it
     * before you hand in.
     */
    private static boolean loginSucceeded(Scanner scanner) {
        return true;
    }

    private static void handleNewMessage(Scanner scanner, int messageNumber) {
        String recipient;
        while (true) {
            System.out.print("Recipient cell number (e.g. +27718693002): ");
            recipient = scanner.nextLine().trim();
            String result = QuickChatMessage.checkRecipientCell(recipient);
            System.out.println(result);
            if (result.equals(QuickChatMessage.CELL_SUCCESS)) {
                break;
            }
        }

        String text;
        while (true) {
            System.out.print("Enter your message (max 250 characters): ");
            text = scanner.nextLine();
            String result = QuickChatMessage.checkMessageLength(text);
            System.out.println(result);
            if (result.equals(QuickChatMessage.LENGTH_OK)) {
                break;
            }
        }

        QuickChatMessage message = new QuickChatMessage(messageNumber, recipient, text);
        System.out.println("Message ID generated: " + message.getMessageId());

        int option = 0;
        while (option < 1 || option > 3) {
            System.out.println("1) Send Message");
            System.out.println("2) Disregard Message");
            System.out.println("3) Store Message to send later");
            System.out.print("Choose an option: ");
            try {
                option = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                option = 0;
            }
        }

        System.out.println(message.sentMessage(option));

        if (option == 1) {
            System.out.println();
            System.out.println(message.getDetails());
        } else if (option == 2) {
            String answer = "";
            while (!answer.equals("0")) {
                answer = scanner.nextLine().trim();
            }
            System.out.println("Message deleted.");
        }
    }

    private static int readPositiveInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value > 0) {
                    return value;
                }
            } catch (NumberFormatException e) {
                // fall through and ask again
            }
            System.out.println("Please enter a whole number greater than 0.");
        }
    }
}