package com.ceglorenzo.postgresqlspring.user;

import lombok.Data;

@Data
public class ResetPasswordRequest {

    private String newPassword;
    private String confirmationPassword;
    private String ResetToken;


}
