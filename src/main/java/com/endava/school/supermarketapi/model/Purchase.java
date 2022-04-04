package com.endava.school.supermarketapi.model;

import com.endava.school.supermarketapi.common.enums.PaymentType;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Purchase {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "supermarket_id")
    private Supermarket superMarketId;
    @NotEmpty
    @ManyToMany(cascade = CascadeType.ALL)
    @Column(name = "item")
    private List<Item> items;
    @NotNull
    @Column(name = "payment_type")
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;
    @Column(name = "cash_amount")
    private double cashAmount;
}