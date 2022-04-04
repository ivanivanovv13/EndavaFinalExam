package com.endava.school.supermarketapi.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseSearchCriteria {
    private String supermarket_id;
    private double cash_amount;
    private String item_id;
    private String payment_type;
}
