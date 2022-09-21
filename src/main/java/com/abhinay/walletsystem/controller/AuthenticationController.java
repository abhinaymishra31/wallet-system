package com.abhinay.walletsystem.controller;

import com.abhinay.walletsystem.jwtimpl.AuthenticationRequest;
import com.abhinay.walletsystem.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody AuthenticationRequest authenticationRequest)
    {
        logger.debug("UserLogin processing started");
        if (authenticationService.userLogin(authenticationRequest) == null) {
            logger.info("Authentication failed");
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        logger.info("Authentication successful. User logged in");
        logger.debug("UserLogin processing finished");
        return new ResponseEntity<>(authenticationService.userLogin(authenticationRequest), HttpStatus.OK);

    }

    @PreAuthorize("hasRole(PERSONAL)")
    @GetMapping("/welcome")
    public String sayWelcome() {
        logger.info("User has Successfully logged in. Welcome message");
        return "Congrats... you have been authorized ";
    }

}

