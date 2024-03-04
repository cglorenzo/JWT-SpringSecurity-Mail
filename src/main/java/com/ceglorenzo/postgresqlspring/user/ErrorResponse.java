package com.ceglorenzo.postgresqlspring.user;

import com.ceglorenzo.postgresqlspring.auth.RegisterRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {

    private String message;
    private RegisterRequest request;

}
