package com.endava.school.supermarketapi.service.impl;

import com.endava.school.supermarketapi.common.enums.ItemType;
import com.endava.school.supermarketapi.common.enums.PaymentType;
import com.endava.school.supermarketapi.dto.PurchaseDto;
import com.endava.school.supermarketapi.dto.PurchaseDtoResponse;
import com.endava.school.supermarketapi.exception.ItemNotFoundException;
import com.endava.school.supermarketapi.exception.MoneyAreNotEnoughException;
import com.endava.school.supermarketapi.exception.SupermarketNotFoundException;
import com.endava.school.supermarketapi.model.*;
import com.endava.school.supermarketapi.repository.ItemRepository;
import com.endava.school.supermarketapi.repository.PurchaseCriteriaRepository;
import com.endava.school.supermarketapi.repository.PurchaseRepository;
import com.endava.school.supermarketapi.repository.SupermarketRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.endava.school.supermarketapi.common.ExceptionMessages.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@RunWith(MockitoJUnitRunner.class)
class PurchaseServiceImplTest {
    Supermarket supermarket;

    List<Purchase> purchases;
    @Mock
    private PurchaseRepository purchaseRepository;
    @Mock
    private SupermarketRepository supermarketRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private PurchaseCriteriaRepository purchaseCriteriaRepository;
    @InjectMocks
    private PurchaseServiceImpl underTest;
    private List<Item> items;
    private List<String> itemIds;
    private Item itemOne;
    private Item itemTwo;
    private Purchase purchaseOne;
    private Purchase purchaseTwo;

    private String expectedCsvResult = "\"Supermarket name, Supermarket address, Supermarket working hours, Supermarket phone number, Item name,Item price, Item type,Total money,Given money,Change,Time of payment\"" + System.lineSeparator();

    @BeforeEach
    public void setUp() {
        items = new ArrayList<>();
        itemIds = new ArrayList<>();
        purchases = new ArrayList<>();
        itemOne = new Item(UUID.randomUUID().toString(), "Donut", 0.89, ItemType.FOOD);
        itemTwo = new Item(UUID.randomUUID().toString(), "Coca Cola", 1.45, ItemType.DRINKS);
        items.add(itemOne);
        items.add(itemTwo);
        supermarket = new Supermarket(UUID.randomUUID().toString(), "Fantastico", "Sofia,Veliko Turnovo,73", "0882421474", "08:00-22:00", items);
        purchaseOne = new Purchase(UUID.randomUUID().toString(), supermarket, items, PaymentType.CASH, 20, 100, LocalTime.now());
        purchaseTwo = new Purchase(UUID.randomUUID().toString(), supermarket, items, PaymentType.CASH, 20, 50, LocalTime.now());
    }

    @Test
    void buyItemsFromSupermarketShouldReturnPurchase() {
        Mockito.when(supermarketRepository.findById(supermarket.getId())).thenReturn(Optional.of(supermarket));
        Mockito.when(purchaseRepository.save(Mockito.any())).thenReturn(purchaseOne);
        Mockito.when(itemRepository.findById(itemOne.getId())).thenReturn(Optional.of(itemOne));
        Mockito.when(itemRepository.findById(itemTwo.getId())).thenReturn(Optional.of(itemTwo));
        PurchaseDto purchaseDto = new PurchaseDto(supermarket.getId(), itemIds, "CASH", 100);
        itemIds.add(itemOne.getId());
        itemIds.add(itemTwo.getId());
        PurchaseDtoResponse purchaseDtoResponse = underTest.buyItemsFromSupermarket(purchaseDto);
        assertThat(purchaseDtoResponse.getItemsDto().size()).isEqualTo(purchaseDto.getItemsIds().size());
        assertThat(purchaseDtoResponse.getPaymentType().toString()).isEqualTo(purchaseDto.getPaymentType());
        assertThat(purchaseDtoResponse.getMoneyGiven()).isEqualTo(purchaseDto.getCashAmount());
    }

    @Test
    void buyItemsFromSupermarketShouldThrowSupermarketNotFound() {
        Mockito.when(supermarketRepository.findById(supermarket.getId())).thenThrow(new SupermarketNotFoundException(SUPERMARKET_NOT_FOUND));
        PurchaseDto purchaseDto = new PurchaseDto(supermarket.getId(), itemIds, "CASH", 100);
        Throwable exception = assertThrows(SupermarketNotFoundException.class, () -> underTest.buyItemsFromSupermarket(purchaseDto));
        assertThat(SUPERMARKET_NOT_FOUND).isEqualTo(exception.getMessage());
    }

    @Test
    void buyItemsFromSupermarketShouldThrowItemNotFound() {
        Mockito.when(supermarketRepository.findById(supermarket.getId())).thenReturn(Optional.of(supermarket));
        Mockito.when(purchaseRepository.save(Mockito.any())).thenReturn(purchaseOne);
        Mockito.when(itemRepository.findById(itemOne.getId())).thenThrow(new ItemNotFoundException(ITEM_NOT_FOUND));
        PurchaseDto purchaseDto = new PurchaseDto(supermarket.getId(), itemIds, "CASH", 100);
        itemIds.add(itemOne.getId());
        itemIds.add(itemTwo.getId());
        Throwable exception = assertThrows(ItemNotFoundException.class, () -> underTest.buyItemsFromSupermarket(purchaseDto));
        assertThat(ITEM_NOT_FOUND).isEqualTo(exception.getMessage());
    }

    @Test
    void getAllPurchases() {
        Mockito.when(purchaseRepository.findAll()).thenReturn(purchases);
        List<PurchaseDtoResponse> purchaseDtoResponses = underTest.getAllPurchases();
        assertThat(purchaseDtoResponses.size()).isEqualTo(purchases.size());
    }

    @Test
    void getAllPurchasesThrowExceptionInefficientFunds() {
        Purchase purchaseThree = new Purchase(UUID.randomUUID().toString(), supermarket, items, PaymentType.CASH, 20, 10, LocalTime.now());
        purchases.add(purchaseThree);
        Mockito.when(purchaseRepository.findAll()).thenReturn(purchases);
        Throwable exception = assertThrows(MoneyAreNotEnoughException.class, () -> underTest.getAllPurchases());
        assertThat(MONEY_NOT_ENOUGH).isEqualTo(exception.getMessage());
    }

    @Test
    void writePurchaseToCSV() throws IOException {
        Mockito.when(purchaseRepository.findAll()).thenReturn(purchases);
        Writer writer = new StringWriter();
        underTest.writePurchaseToCSV(writer);
        assertThat(writer.toString()).isEqualTo(expectedCsvResult);
    }

    @Test
    void findAllFilteredPurchases() {
        purchases.add(purchaseOne);
        purchases.add(purchaseTwo);
        PurchasePage purchasePage= new PurchasePage();
        PurchaseSearchCriteria purchaseSearchCriteria = new PurchaseSearchCriteria();
        Mockito.when(purchaseCriteriaRepository.findAllWithFilters(purchasePage, purchaseSearchCriteria)).thenReturn(new PageImpl<>(purchases));
        Page<PurchaseDtoResponse> purchaseDtoResponse=underTest.findAllFilteredPurchases(purchasePage,purchaseSearchCriteria);
        assertThat(purchaseDtoResponse.getTotalElements()).isEqualTo(purchases.size());
    }
}