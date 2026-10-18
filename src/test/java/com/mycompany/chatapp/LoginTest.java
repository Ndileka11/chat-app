package com.mycompany.chatapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    // ⚠️ PLACEHOLDER TEST DATA — replace with the EXACT strings from your
    // assignment brief's test data table before submitting.

    @Test
    void checkUserName_correctlyFormatted_returnsTrue() {
        Login login = new Login("kyl_1", "Ch&&sec92!", "+27838968976", "Kyle", "Smith");
        assertTrue(login.checkUserName());
    }

    @Test
    void checkUserName_incorrectlyFormatted_returnsFalse() {
        Login login = new Login("kyle!!!!!!!", "Ch&&sec92!", "+27838968976", "Kyle", "Smith");
        assertFalse(login.checkUserName());
    }

    @Test
    void checkPasswordComplexity_meetsRequirements_returnsTrue() {
        Login login = new Login("kyl_1", "Ch&&sec92!", "+27838968976", "Kyle", "Smith");
        assertTrue(login.checkPasswordComplexity());
    }

    @Test
    void checkPasswordComplexity_doesNotMeetRequirements_returnsFalse() {
        Login login = new Login("kyl_1", "password", "+27838968976", "Kyle", "Smith");
        assertFalse(login.checkPasswordComplexity());
    }

    @Test
    void checkCellPhoneNumber_correctlyFormatted_returnsTrue() {
        Login login = new Login("kyl_1", "Ch&&sec92!", "+27838968976", "Kyle", "Smith");
        assertTrue(login.checkCellPhoneNumber());
    }

    @Test
    void checkCellPhoneNumber_incorrectlyFormatted_returnsFalse() {
        Login login = new Login("kyl_1", "Ch&&sec92!", "0838968976", "Kyle", "Smith");
        assertFalse(login.checkCellPhoneNumber());
    }

    @Test
    void registerUser_allValid_returnsSuccessMessage() {
        Login login = new Login("kyl_1", "Ch&&sec92!", "+27838968976", "Kyle", "Smith");
        assertEquals("Username successfully captured. Password successfully captured.",
                login.registerUser());
    }

    @Test
    void registerUser_badUsername_returnsUsernameError() {
        Login login = new Login("kyle!!!!!!!", "Ch&&sec92!", "+27838968976", "Kyle", "Smith");
        assertEquals("Username is not correctly formatted; please ensure that your username "
                   + "contains an underscore and is no more than five characters long.",
                login.registerUser());
    }

    @Test
    void registerUser_badPassword_returnsPasswordError() {
        Login login = new Login("kyl_1", "password", "+27838968976", "Kyle", "Smith");
        assertEquals("Password is not correctly formatted; please ensure that the password "
                   + "contains at least eight characters, a capital letter, a number, "
                   + "and a special character.",
                login.registerUser());
    }

    @Test
    void loginUser_correctCredentials_returnsTrue() {
        Login login = new Login("kyl_1", "Ch&&sec92!", "+27838968976", "Kyle", "Smith");
        assertTrue(login.loginUser("kyl_1", "Ch&&sec92!"));
    }

    @Test
    void loginUser_incorrectCredentials_returnsFalse() {
        Login login = new Login("kyl_1", "Ch&&sec92!", "+27838968976", "Kyle", "Smith");
        assertFalse(login.loginUser("kyl_1", "wrongpassword"));
    }

    @Test
    void returnLoginStatus_success_returnsWelcomeMessage() {
        Login login = new Login("kyl_1", "Ch&&sec92!", "+27838968976", "Kyle", "Smith");
        assertEquals("Welcome Kyle, Smith, it is great to see you again.",
                login.returnLoginStatus(true));
    }

    @Test
    void returnLoginStatus_failure_returnsErrorMessage() {
        Login login = new Login("kyl_1", "Ch&&sec92!", "+27838968976", "Kyle", "Smith");
        assertEquals("Username or password incorrect, please try again.",
                login.returnLoginStatus(false));
    }
}