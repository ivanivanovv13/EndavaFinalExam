package com.endava.school.supermarketapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class addItemsToSupermarketDtoResponse {
    private String supermarketId;
    private List<String> itemsNames;
}
