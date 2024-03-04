package com.ceglorenzo.postgresqlspring.controllers;

import com.ceglorenzo.postgresqlspring.repositories.UserRepository;
import com.ceglorenzo.postgresqlspring.services.EmailService;
import com.ceglorenzo.postgresqlspring.services.UserService;
import com.ceglorenzo.postgresqlspring.user.User;
import com.ceglorenzo.postgresqlspring.user.ChangePasswordRequest;
import com.ceglorenzo.postgresqlspring.user.ForgotPasswordRequest;
import com.ceglorenzo.postgresqlspring.user.ResetPasswordRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/password")
public class ChangePasswordController {


    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailSender;

    private final UserService userService;

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request, Principal connectedUser) {

       // userService.changePassword(request, connectedUser);


        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong password");
        }

        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Passwords are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        repository.save(user);

        // update the authentication object with the new credentials
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return ResponseEntity.ok("Password changed successfully");
    }

    //--------------------------------------------------------------------------------------------------------------------
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        // Buscar el usuario por su correo electrónico
        var userOptionalForgot = repository.findByEmail(request.getEmail());
        if (userOptionalForgot.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with this email not found");
        }

            User user = userOptionalForgot.get();

            // Generar un token de restablecimiento de contraseña
            String resetToken = UUID.randomUUID().toString();
            user.setResetToken(resetToken);
            repository.save(user);

            // Enviar el correo electrónico con el enlace de restablecimiento de contraseña
            String resetLink = "http://localhost:9090/api/v1/password/reset-password?token=" + resetToken;
            String emailBody = "Dear " + user.getUsername() + ",\n\n"
                    + "To reset your password, please click the link below:\n\n" + resetLink + "\n\n"
                    + "If you did not request this, please ignore this email.\n\n" + "Regards,\nThe Team";

            emailSender.send(user.getEmail(), emailBody);


            return ResponseEntity.ok("Password reset email sent successfully");

        }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {

        // Buscar el usuario por el token de restablecimiento de contraseña
        Optional<User> userOptionalReset = repository.findByResetToken(request.getResetToken());
        if (userOptionalReset.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired reset token");
        }
        // Obtener el objeto User del Optional
        User userReset = userOptionalReset.get();

        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Passwords are not the same");
        }

        // Actualizar la contraseña
        userReset.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userReset.setResetToken(null); // Resetear el token después de cambiar la contraseña
        repository.save(userReset);

        return ResponseEntity.ok("Password reset successfully");
    }

}
