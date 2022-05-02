package com.endava.school.supermarketapi.repository;

import com.endava.school.supermarketapi.common.enums.ItemType;
import com.endava.school.supermarketapi.common.enums.PaymentType;
import com.endava.school.supermarketapi.model.*;
import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;
import org.hibernate.query.internal.QueryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@RunWith(MockitoJUnitRunner.class)
class PurchaseCriteriaRepositoryTest {


    @Mock
    EntityManager entityManager; // This mock should be injected in the class that is been tested
    @Mock
    CriteriaBuilder criteriaBuilder;

    @InjectMocks
    PurchaseCriteriaRepository underTest; //System Under Test

    PurchasePage purchasePage;
    PurchaseSearchCriteria purchaseSearchCriteria;

    List<Purchase> purchases;
    List<Item> items;
    Purchase purchaseOne;
    Purchase purchaseTwo;
    Item itemOne;
    Item itemTwo;


    @BeforeEach
    public void setUp() {
        items = new ArrayList<>();
        purchases = new ArrayList();
        itemOne = new Item(UUID.randomUUID().toString(), "Donut", 0.89, ItemType.FOOD);
        itemTwo = new Item(UUID.randomUUID().toString(), "Coca Cola", 1.45, ItemType.DRINKS);
        items.add(itemOne);
        items.add(itemTwo);
        purchaseOne = new Purchase(UUID.randomUUID().toString(), new Supermarket(UUID.randomUUID().toString(), "Fantastico", "Sofia,Veliko Turnovo,73", "0882421474", "08:00-22:00", items), items, PaymentType.CASH, 20, 100, LocalTime.now());
        purchaseTwo = new Purchase(UUID.randomUUID().toString(), new Supermarket(UUID.randomUUID().toString(), "Fantastico", "Sofia,Veliko Turnovo,73", "0882421474", "08:00-22:00", items), items, PaymentType.CASH, 20, 50, LocalTime.now());
        purchases.add(purchaseOne);
        purchases.add(purchaseTwo);
    }

    @Test
    void findAllWithFilters() {
        underTest.findAllWithFilters(purchasePage, purchaseSearchCriteria);
    }
}