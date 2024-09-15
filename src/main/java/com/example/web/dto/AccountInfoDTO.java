package com.example.web.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Getter
@Setter

public class AccountInfoDTO {
    private String firstName;
    private String lastName;
    private String contactIdentify;
    private String address;
    private Date dateOfBirth;
    private String sex;
    private String password;
}
