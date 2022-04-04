package com.endava.school.supermarketapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Supermarket {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    @Column(name = "name")
    @NotBlank
    private String name;
    @Column(name = "address")
    @NotBlank
    private String address;
    @Column(name = "phoneNumber")
    @Size(max = 10)
    private String phoneNumber;
    @Column(name = "work_hours")
    private String workHours;
    @ManyToMany
    @JoinTable(name = "supermarket_items",
            joinColumns = @JoinColumn(name = "supermarket_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Item> items;
}
