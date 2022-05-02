package com.endava.school.supermarketapi.dto;

import com.endava.school.supermarketapi.anotation.EnumValidatorConstraint;
import com.endava.school.supermarketapi.common.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @EnumValidatorConstraint(enumClass = PaymentType.class)
    private String paymentType;
    private double cashAmount;

}
