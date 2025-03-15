package com.ddmtchr.blpslab1.controller;

import com.ddmtchr.blpslab1.dto.request.LoginRequest;
import com.ddmtchr.blpslab1.dto.request.RegisterRequest;
import com.ddmtchr.blpslab1.dto.response.JwtResponse;
import com.ddmtchr.blpslab1.exception.UsernameAlreadyExistsException;
import com.ddmtchr.blpslab1.security.entity.User;
import com.ddmtchr.blpslab1.security.jwt.JwtUtils;
import com.ddmtchr.blpslab1.security.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(generateJwtResponse(request.getUsername(), request.getPassword()));
    }

    @PostMapping("/register")
    public ResponseEntity<JwtResponse> registerUser(@RequestBody @Valid RegisterRequest request) {
        if (userService.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }
        // костыль
        String unEncodedPassword = request.getPassword();
        request.setPassword(encoder.encode(unEncodedPassword));
        userService.addUser(request);

        return new ResponseEntity<>(generateJwtResponse(request.getUsername(), unEncodedPassword), HttpStatus.CREATED);
    }

    private JwtResponse generateJwtResponse(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        User userDetails = (User) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();
        return new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), roles);
    }
}
