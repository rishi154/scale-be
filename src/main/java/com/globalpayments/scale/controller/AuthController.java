package com.globalpayments.scale.controller;

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
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserService userService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtProvider jwtProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@Valid @RequestBody AuthRequest authRequest) {
        log.debug("Inside ..............");
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        authRequest.getEmail(),
//                        authRequest.getPassword()
//                )
//        );
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
        //String jwt = jwtProvider.generateToken(authentication);

        // Get user information
        UserDto userDto = userService.getUserByEmail(authRequest.getEmail());

        String jwt = "Sdfdfldooidsuf98ewr984wt0dijgldskfpweuofdjl";
        UserDto tempUserDTO = new UserDto();
        tempUserDTO.setUserId("gpn_001");
        tempUserDTO.setName("Rushikesh");
        tempUserDTO.setRole(User.UserRole.ADMIN);
        return ResponseEntity.ok(new AuthResponse(jwt, tempUserDTO));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
        UserDto userDto = userService.createUser(createUserRequest);
        return ResponseEntity.ok(userDto);
    }
}
