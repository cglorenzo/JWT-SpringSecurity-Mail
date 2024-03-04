package com.ceglorenzo.postgresqlspring.services;

import org.springframework.stereotype.Service;

@Service
public class PasswordCheckService {

    private static final String LENGTH_REGEX = ".{8,}";
    private static final String UPPERCASE_REGEX = ".*[A-Z].*";
    private static final String LOWERCASE_REGEX = ".*[a-z].*";
    private static final String DIGIT_REGEX = ".*\\d.*";
    private static final String SPECIAL_CHARACTER_REGEX = ".*[!@#$%^&*()-+=?].*";
    public boolean checkPassword(String password) {
        return password.matches(LENGTH_REGEX) &&
                password.matches(UPPERCASE_REGEX) &&
                password.matches(LOWERCASE_REGEX) &&
                password.matches(DIGIT_REGEX) &&
                password.matches(SPECIAL_CHARACTER_REGEX);
    }

    public boolean twoPasswordsMatch(String password, String confirmationPassword) {
        return password.equals(confirmationPassword);
    }
}
