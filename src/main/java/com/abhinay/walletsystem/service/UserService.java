package com.abhinay.walletsystem.service;

import com.abhinay.walletsystem.dto.AddWalletBalanceDTO;
import com.abhinay.walletsystem.dto.PassBookDTO;
import com.abhinay.walletsystem.dto.SendWalletBalanceDTO;
import com.abhinay.walletsystem.dto.UpdateDetailDTO;
import com.abhinay.walletsystem.model.UserInfo;

public interface UserService {

    UserInfo userSignUp(UserInfo userInfo);

    UserInfo userUpdate(String emailId, UpdateDetailDTO updateDetailDTO);

    UserInfo getUserDetail(String emailId);

    UserInfo addBalanceToWallet(String emailId, AddWalletBalanceDTO addWalletBalanceDTO);

    UserInfo sendBalanceFromWallet(String emailId, SendWalletBalanceDTO sendWalletBalanceDTO);

    PassBookDTO viewPassBook(String emailId);


}
