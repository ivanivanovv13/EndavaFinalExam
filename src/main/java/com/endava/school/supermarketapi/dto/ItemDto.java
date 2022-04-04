package com.endava.school.supermarketapi.dto;

import com.endava.school.supermarketapi.common.enums.ItemType;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    @NotBlank
    @Size(max = 64)
    private String name;
    @NotNull
    private double price;
    @NotNull
    @Enumerated
    private ItemType itemType;
}
