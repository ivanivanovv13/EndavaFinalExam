package com.endava.school.supermarketapi.dto;

import com.endava.school.supermarketapi.common.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseDto {
    @NotBlank
    private String superMarketId;
    @NotEmpty
    private List<String> itemsIds;
    @NotNull
    @Enumerated
    private PaymentType paymentType;
    private double cashAmount;

}
