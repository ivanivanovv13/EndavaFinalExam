package com.endava.school.supermarketapi.model;

import com.endava.school.supermarketapi.common.enums.PaymentType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalTime;

@Getter
@Setter
public class PurchaseSearchCriteria {
    private String supermarketId;
    private String supermarketName;
    private String supermarketPhoneNumber;
    private String supermarketAddress;
    private String supermarketWorkHours;
    private String itemId;
    private String itemName;
    private double itemPrice;
    private String itemType;
    private PaymentType paymentType;
    private double totalCashAmount;
}
