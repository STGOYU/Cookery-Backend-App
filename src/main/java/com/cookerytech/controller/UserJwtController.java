package com.cookerytech.controller;


import com.cookerytech.config.EmailConfig;
import com.cookerytech.dto.UserDTO;
import com.cookerytech.dto.request.ForgotPasswordRequest;
import com.cookerytech.dto.request.LoginRequest;
import com.cookerytech.dto.request.RegisterRequest;
import com.cookerytech.dto.request.ResetPasswordRequest;
import com.cookerytech.dto.response.*;
import com.cookerytech.security.jwt.*;
import com.cookerytech.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.*;

@RestController
public class UserJwtController {


    private final JwtUtils jwtUtils;


    private final UserService userService;



    private final AuthenticationManager authenticationManager;//ilk karsilayacak olan

    public UserJwtController(JwtUtils jwtUtils, UserService userService, AuthenticationManager authenticationManager) {
        this.jwtUtils = jwtUtils;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }


    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@Valid
                                                   @RequestBody RegisterRequest registerRequest) {
        UserDTO userDTO=  userService.saveUser(registerRequest);

        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }



    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@Valid
                                                      @RequestBody LoginRequest loginRequest) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                        loginRequest.getPassword());

        Authentication authentication =
                authenticationManager.authenticate(usernamePasswordAuthenticationToken);


        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwtToken = jwtUtils.generateJwtToken(userDetails);

        LoginResponse loginResponse = new LoginResponse(jwtToken);

        return new ResponseEntity<>(loginResponse, HttpStatus.OK);

    }
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        // Şifre sıfırlama isteği işlemleri
          String token= userService.createPasswordResetToken(forgotPasswordRequest.getEmail());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        // Şifre sıfırlama işlemleri
        userService.resetPassword(resetPasswordRequest);
        return ResponseEntity.ok("Password reset successfully.");
    }
}