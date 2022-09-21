package com.abhinay.walletsystem.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserAuth {
    private String emailId;
    private String password;
    private UserType userType;

    public UserAuth(UserInfo userInfo)
    {
        this.emailId=userInfo.getEmailId();
        this.password=userInfo.getPassword();
        this.userType=userInfo.getUserType();
    }
}
