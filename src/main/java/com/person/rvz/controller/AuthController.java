package com.person.rvz.controller;

import com.person.rvz.dto.LoginRequest;
import com.person.rvz.dto.LoginResponse;
import com.person.rvz.dto.PasswordUpdateRequest;
import com.person.rvz.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        String token = loginService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PutMapping("/users/{username}/password")
    public ResponseEntity<Void> updatePassword(
            HttpServletRequest httpRequest,
            @PathVariable String username,
            @RequestBody PasswordUpdateRequest request) {
        String requesterUsername = (String) httpRequest.getAttribute("username");

        loginService.updatePassword(requesterUsername, username, request.getNewPassword());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
