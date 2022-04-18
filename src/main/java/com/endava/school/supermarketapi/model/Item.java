package com.endava.school.supermarketapi.model;

import com.endava.school.supermarketapi.common.enums.ItemType;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    @Column(name = "name")
    @NotBlank
    private String name;
    @Column(name = "price")
    @NotNull
    private double price;
    @Column(name = "item_type")
    @NotNull
    @Enumerated(EnumType.STRING)
    private ItemType itemType;
}