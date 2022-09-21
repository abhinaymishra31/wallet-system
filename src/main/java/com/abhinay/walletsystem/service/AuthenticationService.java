package com.abhinay.walletsystem.service;

import com.abhinay.walletsystem.jwtimpl.AuthenticationRequest;
import com.abhinay.walletsystem.jwtimpl.AuthenticationResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthenticationService extends UserDetailsService {

    AuthenticationResponse userLogin(AuthenticationRequest authenticationRequest);

}
