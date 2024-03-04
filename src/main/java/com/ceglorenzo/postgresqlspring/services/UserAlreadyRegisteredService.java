package com.ceglorenzo.postgresqlspring.services;

import com.ceglorenzo.postgresqlspring.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAlreadyRegisteredService {

    private final UserRepository userRepository;

    public boolean isUserAlreadyRegistered(String email){
        return userRepository.findByEmail(email).isPresent();
    }


}
