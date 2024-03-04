package com.ceglorenzo.postgresqlspring.services;


public interface EmailSender {

    void send(String to, String email);
}
