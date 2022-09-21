package com.abhinay.walletsystem.dto;

import com.abhinay.walletsystem.model.UserType;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateDetailDTO {
    private String firstName;
    private String lastName;
    private String contactNo;
    @Enumerated(EnumType.STRING)
    private UserType userType;
    private String password;
}
