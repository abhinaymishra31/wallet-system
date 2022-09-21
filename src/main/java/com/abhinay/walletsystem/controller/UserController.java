package com.abhinay.walletsystem.controller;

import com.abhinay.walletsystem.dto.AddWalletBalanceDTO;
import com.abhinay.walletsystem.dto.PassBookDTO;
import com.abhinay.walletsystem.dto.SendWalletBalanceDTO;
import com.abhinay.walletsystem.dto.UpdateDetailDTO;
import com.abhinay.walletsystem.jwtimpl.JwtUtil;
import com.abhinay.walletsystem.model.UserInfo;
import com.abhinay.walletsystem.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1")
@NoArgsConstructor
@AllArgsConstructor
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/createuser")
    public ResponseEntity<UserInfo> userRegister(@RequestBody UserInfo userInfo)
    {
        logger.debug("User register process started");
        userInfo.setPassword(this.bCryptPasswordEncoder.encode(userInfo.getPassword()));
        return new ResponseEntity<>(userService.userSignUp(userInfo), HttpStatus.CREATED);
    }

    @PutMapping("/updateuser")
    public ResponseEntity<UserInfo> userUpdate(@RequestBody UpdateDetailDTO updateDetailDTO)
    {
        logger.debug("update user process started");
        updateDetailDTO.setPassword(this.bCryptPasswordEncoder.encode(updateDetailDTO.getPassword()));
        String emailId= jwtUtil.extractUsername(httpServletRequest.getHeader("Authorization").split(" ")[1].trim());
        logger.info("User id: ",emailId);
        return new ResponseEntity<>(userService.userUpdate(emailId,updateDetailDTO), HttpStatus.OK);
    }

    @PutMapping("/user/balance/add")
    public ResponseEntity<UserInfo> addBalanceToWallet(@RequestBody AddWalletBalanceDTO addWalletBalanceDTO)
    {
        logger.debug("Adding balance to wallet from bank account process started");
        String emailId= jwtUtil.extractUsername(httpServletRequest.getHeader("Authorization").split(" ")[1].trim());
        logger.info("User id: ",emailId);
        return new ResponseEntity<>(userService.addBalanceToWallet(emailId, addWalletBalanceDTO),HttpStatus.OK);
    }

    @PutMapping("/user/balance/send")
    public ResponseEntity<UserInfo> sendBalanceFromWallet(@RequestBody SendWalletBalanceDTO sendWalletBalanceDTO)
    {
        logger.debug("Sending balance from one wallet to other wallet process started");
        String emailId= jwtUtil.extractUsername(httpServletRequest.getHeader("Authorization").split(" ")[1].trim());
        logger.info("from [{}] to [{}] | amount: [{}] ",emailId,sendWalletBalanceDTO.getToPersonId(),sendWalletBalanceDTO.getAmount());
        return new ResponseEntity<>(userService.sendBalanceFromWallet(emailId, sendWalletBalanceDTO),HttpStatus.OK);
    }


    @GetMapping("/user/detail")
    public ResponseEntity<UserInfo> getUserDetail()
    {
        logger.debug("user details fetching process started");
        String emailId= jwtUtil.extractUsername(httpServletRequest.getHeader("Authorization").split(" ")[1].trim());
        logger.info("user id: ",emailId);
        return new ResponseEntity<>(userService.getUserDetail(emailId), HttpStatus.FOUND);
    }

    @GetMapping("/user/passbook")
    public ResponseEntity<PassBookDTO> viewPassBook()
    {
        logger.debug("Passbook fetching process started");
        String emailId= jwtUtil.extractUsername(httpServletRequest.getHeader("Authorization").split(" ")[1].trim());
        logger.info("user id: ",emailId);
        return new ResponseEntity<>(userService.viewPassBook(emailId),HttpStatus.OK);
    }



}
