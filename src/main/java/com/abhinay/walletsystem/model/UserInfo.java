package com.abhinay.walletsystem.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "user_info")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

    @Id
    @Column(name = "email_id")
    private String emailId;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "contact_no")
    private String contactNo;
    @Column(name = "dob")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dob;
    @Column(name = "account_no")
    private String accountNo;
    @Column(name = "account_balance")
    private Double accountBalance;
    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private UserType userType;
    @Column(name = "wallet_balance")
    private Double walletBalance;
    @Column(name = "password")
    private String password;
}
