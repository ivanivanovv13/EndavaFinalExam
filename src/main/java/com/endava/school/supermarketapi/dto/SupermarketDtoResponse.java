package com.endava.school.supermarketapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupermarketDtoResponse {
    private String name;
    private String address;
    private String phoneNumber;
    private String workHours;
    private List<ItemDto> itemsDto;
}
