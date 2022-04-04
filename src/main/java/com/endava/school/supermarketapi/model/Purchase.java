package com.endava.school.supermarketapi.model;

import com.endava.school.supermarketapi.common.enums.PaymentType;
import com.sun.istack.NotNull;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;

@Entity
public class Purchase {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    @NotBlank
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "supermarket_id")
    private Supermarket superMarketId;
    @NotBlank
    @OneToMany(cascade = CascadeType.ALL)
    @Column(name = "item")
    private List<Item> items;
    @NotNull
    @Column(name = "payment_type")
    private PaymentType paymentType;
    @Column(name = "cash_amount")
    private double cashAmount;
}