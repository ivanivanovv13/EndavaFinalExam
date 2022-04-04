package com.endava.school.supermarketapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupermarketDto {
    @NotBlank
    private String name;
    @NotBlank
    private String address;
    @Size(max = 10)
    private String phoneNumber;
    @NotBlank
    private String workHours;
}