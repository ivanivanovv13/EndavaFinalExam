package com.endava.school.supermarketapi.service.impl;

import com.endava.school.supermarketapi.common.enums.PaymentType;
import com.endava.school.supermarketapi.dto.ItemDto;
import com.endava.school.supermarketapi.dto.PurchaseDto;
import com.endava.school.supermarketapi.dto.PurchaseDtoResponse;
import com.endava.school.supermarketapi.dto.SupermarketDto;
import com.endava.school.supermarketapi.exception.ItemNotFoundException;
import com.endava.school.supermarketapi.exception.MoneyAreNotEnoughException;
import com.endava.school.supermarketapi.exception.SupermarketNotFoundException;
import com.endava.school.supermarketapi.model.*;
import com.endava.school.supermarketapi.repository.ItemRepository;
import com.endava.school.supermarketapi.repository.PurchaseCriteriaRepository;
import com.endava.school.supermarketapi.repository.PurchaseRepository;
import com.endava.school.supermarketapi.repository.SupermarketRepository;
import com.endava.school.supermarketapi.service.PurchaseService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Writer;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.endava.school.supermarketapi.common.ExceptionMessages.*;

@Service
public class PurchaseServiceImpl implements PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final SupermarketRepository supermarketRepository;
    private final ItemRepository itemRepository;
    private final PurchaseCriteriaRepository purchaseCriteriaRepository;
    private final ModelMapper modelMapper;
    private final String csvHeader = "Supermarket name, Supermarket address, Supermarket working hours, Supermarket phone number, Item name,Item price, Item type,Total money,Given money,Change,Time of payment";

    @Autowired
    public PurchaseServiceImpl(PurchaseRepository purchaseRepository, SupermarketRepository supermarketRepository, ItemRepository itemRepository, PurchaseCriteriaRepository purchaseCriteriaRepository) {
        this.purchaseRepository = purchaseRepository;
        this.supermarketRepository = supermarketRepository;
        this.itemRepository = itemRepository;
        this.purchaseCriteriaRepository = purchaseCriteriaRepository;
        this.modelMapper = new ModelMapper();
    }

    @Override
    public PurchaseDtoResponse buyItemsFromSupermarket(PurchaseDto purchaseDto) {
        Supermarket supermarket = supermarketRepository.findById(purchaseDto.getSuperMarketId())
                .orElseThrow(() -> new SupermarketNotFoundException(SUPERMARKET_NOT_FOUND));
        List<Item> items = findItemsByIdFromList(purchaseDto.getItemsIds());
        Purchase newPurchase = purchaseRepository.save(
                new Purchase(UUID.randomUUID().toString(),
                        supermarket,
                        items,
                        PaymentType.valueOf(purchaseDto.getPaymentType()),
                        calculateTotalAmount(items),
                        purchaseDto.getCashAmount(),
                        LocalTime.now()));
        return getPurchaseDtoResponse(newPurchase);
    }

    @Override
    public List<PurchaseDtoResponse> getAllPurchases() {
        return purchaseRepository
                .findAll()
                .stream()
                .map(this::getPurchaseDtoResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void writePurchaseToCSV(Writer writer) throws IOException {
        List<Purchase> purchases = purchaseRepository.findAll();
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            csvPrinter.printRecord(csvHeader);
            for (Purchase purchase : purchases) {
                csvPrinter.printRecord(purchase.getSupermarket().getName(),
                        purchase.getSupermarket().getAddress(),
                        purchase.getSupermarket().getWorkHours(),
                        purchase.getSupermarket().getPhoneNumber(),
                        purchase.getItems().stream().map(item -> item.getName() + "," + item.getPrice() + "," + item.getItemType()).collect(Collectors.joining(",")),
                        purchase.getPaymentType(),
                        purchase.getTotalCashAmount(),
                        purchase.getMoneyGiven(),
                        calculateTheChange(purchase.getMoneyGiven(), purchase.getTotalCashAmount()),
                        purchase.getTimeOfPayment());
            }
        }
    }

    @Override
    public Page<PurchaseDtoResponse> findAllFilteredPurchases(PurchasePage purchasePage, PurchaseSearchCriteria purchaseSearchCriteria) {
        Page<PurchaseDtoResponse> purchasesDto = new PageImpl<>(purchaseCriteriaRepository.findAllWithFilters(purchasePage, purchaseSearchCriteria).
                stream().map(purchase->getPurchaseDtoResponse(purchase)).collect(Collectors.toList()));
        return purchasesDto;
    }

    private PurchaseDtoResponse getPurchaseDtoResponse(Purchase purchase) {
        return new PurchaseDtoResponse(modelMapper.map(purchase.getSupermarket(), SupermarketDto.class),
                mapItemsToItemsDto(purchase.getItems()),
                purchase.getPaymentType(),
                purchase.getTotalCashAmount(),
                purchase.getMoneyGiven(),
                calculateTheChange(purchase.getMoneyGiven(), purchase.getTotalCashAmount()),
                purchase.getTimeOfPayment());
    }

    private List<ItemDto> mapItemsToItemsDto(List<Item> items) {
        List<ItemDto> itemsDto = new ArrayList();
        for (Item item : items) {
            itemsDto.add(modelMapper.map(item, ItemDto.class));
        }
        return itemsDto;
    }

    private List<Item> findItemsByIdFromList(List<String> listOfItemsIds) {
        List<Item> items = new ArrayList<>();
        for (String itemId : listOfItemsIds) {
            Item item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new ItemNotFoundException(ITEM_NOT_FOUND));
            items.add(item);
        }
        return items;
    }

    private double calculateTotalAmount(List<Item> items) {
        double totalAmount = 0;
        for (Item item : items) {
            totalAmount += item.getPrice();
        }

        return totalAmount;
    }

    private double calculateTheChange(double givenMoney, double totalAmount) {
        if (totalAmount > givenMoney) {
            throw new MoneyAreNotEnoughException(MONEY_NOT_ENOUGH);
        }
        return givenMoney - totalAmount;
    }
}
