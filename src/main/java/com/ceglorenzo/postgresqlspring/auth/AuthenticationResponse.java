package com.ceglorenzo.postgresqlspring.auth;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private String refreshToken;
    private String accessToken;
}
