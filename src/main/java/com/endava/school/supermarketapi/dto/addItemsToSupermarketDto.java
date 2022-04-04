package com.endava.school.supermarketapi.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class addItemsToSupermarketDto {
    @NotNull
    private String supermarketId;
    @NotEmpty
    private List<String> itemIds;
}
