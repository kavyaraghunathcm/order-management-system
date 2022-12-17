package com.kav.ordermanagementsvc.dto;


import com.kav.ordermanagementsvc.constants.Level;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Setter
@Getter
@ToString
public class CustomerDto{
    private  Long id;

    @NotBlank
    private String name;
    @Email
    private String email;

    private Level level;
}
