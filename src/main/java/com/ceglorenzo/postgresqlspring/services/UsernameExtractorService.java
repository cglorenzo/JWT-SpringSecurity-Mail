package com.ceglorenzo.postgresqlspring.services;

import org.springframework.stereotype.Service;

@Service
public class UsernameExtractorService {
    public String extractUsernameFromEmail(String email) {
        int atIndex = email.indexOf('@');
        return atIndex != -1 ? email.substring(0, atIndex) : email;
    }
}

