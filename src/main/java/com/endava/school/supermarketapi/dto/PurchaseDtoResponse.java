package com.endava.school.supermarketapi.dto;

import com.endava.school.supermarketapi.common.enums.PaymentType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseDtoResponse {
    @NotBlank
    private SupermarketDto supermarketDto;
    @NotEmpty
    private List<ItemDto> itemsDto;
    @NotNull
    @Enumerated
    private PaymentType paymentType;
    private  double totalCashAmount;
    private double moneyGiven;
    private double change;
    private LocalTime timeOfPayment;
}
