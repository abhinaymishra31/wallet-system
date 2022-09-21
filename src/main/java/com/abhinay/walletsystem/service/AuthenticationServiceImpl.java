package com.abhinay.walletsystem.service;

import com.abhinay.walletsystem.jwtimpl.AuthenticationRequest;
import com.abhinay.walletsystem.jwtimpl.AuthenticationResponse;
import com.abhinay.walletsystem.jwtimpl.JwtUserDetails;
import com.abhinay.walletsystem.jwtimpl.JwtUtil;
import com.abhinay.walletsystem.model.UserInfo;
import com.abhinay.walletsystem.exceptions.PasswordNotMatchedException;
import com.abhinay.walletsystem.exceptions.UserNotRegisteredException;
import com.abhinay.walletsystem.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{
    @Autowired
    private AuthenticationManager authManager;
//    @Autowired
    private JwtUtil jwtUtil;
//    @Autowired
    private UserInfoRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AuthenticationServiceImpl( JwtUtil jwtUtil, UserInfoRepository userRepository) {

        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    //    private static final MyLogger logger=new MyLogger();
    @Override
    public AuthenticationResponse userLogin(AuthenticationRequest authenticationRequest){
//    public Boolean userLogin(User user) {
//        Boolean status = false;
        Optional<UserInfo> findUser = userRepository.findById(authenticationRequest.getUserEmail());
//        Optional<User> findUser = userRepository.findByUserEmailAndPassword(user.getUserEmail(), user.getPassword());
        if (findUser.isPresent()) {
//            if (authenticationRequest.getPassword().equals(findUser.get().getPassword())) {
            if(bCryptPasswordEncoder.matches(authenticationRequest.getPassword(),findUser.get().getPassword())) {
//                commented below for testing only
//                    && user.getUserType().equals(findUser.get().getUserType())) {

                final UserDetails userDetails = this.loadUserByUsername(authenticationRequest.getUserEmail());
                final String jwt = jwtUtil.generateToken(userDetails);
                System.out.println("Jwt is: " + jwt);
                System.out.println("Jwt user name is: " + jwtUtil.extractUsername(jwt));
                try {
                    authManager.authenticate(
                            new UsernamePasswordAuthenticationToken(authenticationRequest.getUserEmail(), authenticationRequest.getPassword()));
//                    new UsernamePasswordAuthenticationToken(findUser.get().getEmailId(), findUser.get().getPassword()));
//                    ,new ArrayList<SimpleGrantedAuthority>(Arrays.asList(new SimpleGrantedAuthority("ROLE_"+user.getUserType().name()))))
//                            );
                } catch (BadCredentialsException bc) {
                    throw new BadCredentialsException("Bad Credentials...");
                }
                return new AuthenticationResponse(authenticationRequest.getUserEmail(), true, jwt);
//                status = true;
            } else {
                throw new PasswordNotMatchedException("Invalid Password... please retry");
            }
        }
        else
        {
            throw new UserNotRegisteredException("Invalid Credentials or User is Not Registered");
        }
//        return new ResponseEntity<>("Please try again.. using the correct credentials",HttpStatus.NOT_FOUND);
    }


    /*@Override
    public String userLogin(User user)
    {
        Optional<User> findUser=userRepository.findById(user.getUserEmail());
        if(findUser.isPresent())
        {
            if(user.getPassword().equals(findUser.get().getPassword())
                    && user.getUserType().equals(findUser.get().getUserType()))
            {
                return "User logged In";
            }
            *//*else
            {
                throw new PasswordNotMatchedException("Invalid Password... please retry");
            }*//*
        }
        else
        {
            throw new UserNotRegisteredException("Invalid Credentials or User is Not Registered");
        }

        return "Please try again.. using the correct credentials";
    }*/


    //    this method will be called iff username is present in the database and all credentials are matched
    @Override
    public UserDetails loadUserByUsername(String username) {
        JwtUserDetails jwtUserDetails;
        System.out.println("Started loadUserByUsername() ");

        UserInfo user = userRepository.findById(username).get();
        jwtUserDetails = new JwtUserDetails(user);
        System.out.println(jwtUserDetails);
        return jwtUserDetails;

        /*return new JwtUserDetails(findUser.orElseThrow(()->{
            System.out.println("inside JwtUserDetails constructor...");
            throw new UsernameNotFoundException("Username does not exists..");
        }));*/

        /*return findUser.map(JwtUserDetails::new).orElseThrow(()->{
            throw new UsernameNotFoundException("Username does not exists");
        });*/
    }
}
