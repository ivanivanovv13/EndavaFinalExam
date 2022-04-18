package com.endava.school.supermarketapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupermarketDto {
    @NotBlank
    private String name;
    @NotBlank
    @Size(max = 128)
    @Pattern(regexp = "^\\w{3,62},[\\w\\s]{3,62},\\d{1,4}$",message = "The address must match format – {city}, {street} {street number")
    private String address;
    @Size(max = 10, message = "The phone number must be 10 digits.")
    @Pattern(regexp = "^08[7-9]\\d{7}$", message = "Not a valid phone number.")
    private String phoneNumber;
    @NotBlank
    @Pattern(regexp = "^\\d{2}:\\d{2}-\\d{2}:\\d{2}$",message = "Work time should match format {HH:mm}-{HH:mm}")
    private String workHours;
}