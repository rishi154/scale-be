package com.globalpayments.scale.controller;

import com.globalpayments.scale.dao.UserDao;
import com.globalpayments.scale.dao.impl.UserDaoImpl;
import com.globalpayments.scale.dto.AuthRequest;
import com.globalpayments.scale.dto.AuthResponse;
import com.globalpayments.scale.dto.CreateUserRequest;
import com.globalpayments.scale.dto.UserDto;
import com.globalpayments.scale.model.User;
import com.globalpayments.scale.security.JwtProvider;
import com.globalpayments.scale.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserService userService;
    private final UserDao userDao;


    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtProvider jwtProvider, UserService userService, UserDao userDao) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.userService = userService;
        this.userDao = userDao;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@Valid @RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                )
        );
//        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        UserDto userDto = userService.getUserByEmail(authRequest.getEmail());
        return ResponseEntity.ok(new AuthResponse(jwt, userDto));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
        UserDto userDto = userService.createUser(createUserRequest);
        return ResponseEntity.ok(userDto);
    }
}
