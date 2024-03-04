package com.ceglorenzo.postgresqlspring.user;


import lombok.Data;

@Data
public class ForgotPasswordRequest {

    private String email;
}
