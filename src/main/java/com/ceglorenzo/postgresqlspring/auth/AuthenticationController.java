package com.ceglorenzo.postgresqlspring.auth;

import com.ceglorenzo.postgresqlspring.services.PasswordCheckService;
import com.ceglorenzo.postgresqlspring.services.UserAlreadyRegisteredService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    private final PasswordCheckService passwordCheckService;
    private final UserAlreadyRegisteredService userAlreadyRegisteredService;

    @PostMapping("/Register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){

        if(!passwordCheckService.checkPassword(request.getPassword())){
            return ResponseEntity.badRequest().build();
        }

        if(!passwordCheckService.twoPasswordsMatch(request.getPassword(), request.getConfirmPassword())){
            return ResponseEntity.badRequest().build();
        }

        if(userAlreadyRegisteredService.isUserAlreadyRegistered(request.getEmail())){
            return ResponseEntity.badRequest().build();
        }

        //TODO
        return ResponseEntity.ok(service.register(request));

    }

    @PostMapping("/Authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){

        //TODO
        return ResponseEntity.ok(service.authenticate(request));
    }

}
