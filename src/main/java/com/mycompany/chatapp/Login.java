package com.mycompany.chatapp;

import java.util.regex.Pattern;

/**
 * Handles user registration and login validation per PROG5121 POE spec.
 */
public class Login {

    private String username;
    private String password;
    private String cellPhoneNumber;
    private String firstName;
    private String lastName;

    public Login(String username, String password, String cellPhoneNumber,
                 String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Username must contain an underscore and be no more than 5 characters long.
     */
    public boolean checkUserName() {
        if (username == null) {
            return false;
        }
        return username.contains("_") && username.length() <= 5;
    }

    /**
     * Password must be at least 8 characters, contain a capital letter,
     * a number, and a special character.
     */
    public boolean checkPasswordComplexity() {
        if (password == null) {
            return false;
        }
        String regex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>_\\-]).{8,}$";
        return Pattern.matches(regex, password);
    }

    /**
     * Cell phone number must be the correct length and contain the
     * South African international country code (+27) followed by 9 digits.
     */
    public boolean checkCellPhoneNumber() {
        if (cellPhoneNumber == null) {
            return false;
        }
        String regex = "^\\+27\\d{9}$";
        return Pattern.matches(regex, cellPhoneNumber);
    }

    /**
     * Registers the user, returning the appropriate message based on
     * username and password validation.
     */
    public String registerUser() {
        if (!checkUserName()) {
            return "Username is not correctly formatted; please ensure that your username "
                 + "contains an underscore and is no more than five characters long.";
        }
        if (!checkPasswordComplexity()) {
            return "Password is not correctly formatted; please ensure that the password "
                 + "contains at least eight characters, a capital letter, a number, "
                 + "and a special character.";
        }
        return "Username successfully captured. Password successfully captured.";
    }

    /**
     * Verifies entered login details match the stored username and password.
     */
    public boolean loginUser(String enteredUsername, String enteredPassword) {
        if (username == null || password == null) {
            return false;
        }
        return username.equals(enteredUsername) && password.equals(enteredPassword);
    }

    /**
     * Returns the appropriate welcome or failure message for a login attempt.
     */
    public String returnLoginStatus(boolean loginSuccessful) {
        if (loginSuccessful) {
            return "Welcome " + firstName + ", " + lastName + ", it is great to see you again.";
        }
        return "Username or password incorrect, please try again.";
    }

    // Getters (useful for tests / other classes)
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getCellPhoneNumber() { return cellPhoneNumber; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
}