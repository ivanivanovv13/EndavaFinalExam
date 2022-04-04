package com.endava.school.supermarketapi.service.impl;

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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
        LocalTime timeOfPayment = LocalTime.now();
        Purchase newPurchase = purchaseRepository.save(new Purchase(UUID.randomUUID().toString(),
                supermarket,
                items,
                purchaseDto.getPaymentType(),
                calculateTotalAmount(items)));
        return new PurchaseDtoResponse(modelMapper.map(supermarket, SupermarketDto.class),
                mapItemsToItemsDto(items),
                purchaseDto.getPaymentType(),
                calculateTheChange(purchaseDto.getCashAmount(), calculateTotalAmount(items)),
                timeOfPayment);
    }

    @Override
    public List<PurchaseDto> getAll() {
        List<Purchase> purchases = purchaseRepository.findAll();
        List<PurchaseDto> purchaseDtos = new ArrayList<>();
        for (Purchase purchase : purchases) {
            List<String> itemsIds = mapItemsToString(purchase.getItems());
            PurchaseDto purchaseDto = modelMapper.map(purchase, PurchaseDto.class);
            purchaseDto.setItemsIds(itemsIds);
            purchaseDtos.add(purchaseDto);
        }
        return purchaseDtos;
    }

    @Override
    public void writePurchaseToCSV() throws IOException {
        List<Purchase> purchases = purchaseRepository.findAll();
        File file = new File("books.csv");
        file.createNewFile();
        try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(file), CSVFormat.DEFAULT)) {
            for (Purchase purchase : purchases) {
                csvPrinter.printRecord(purchase.getSuperMarketId(),
                        purchase.getCashAmount(),
                        purchase.getPaymentType(),
                        purchase.getItems().stream().map(item -> item.getId()).collect(Collectors.toList()));
            }
        }
    }

    @Override
    public Page<PurchaseDto> findAllFileteredPurchases(PurchasePage purchasePage, PurchaseSearchCriteria purchaseSearchCriteria) {
        Page<PurchaseDto> purchasesDto = new PageImpl<>(purchaseCriteriaRepository.findAllWithFilters(purchasePage, purchaseSearchCriteria).
                stream().map(user -> modelMapper.map(user, PurchaseDto.class)).collect(Collectors.toList()));
        return purchasesDto;
    }


    private List<String> mapItemsToString(List<Item> items) {
        List<String> itemsToString = new ArrayList();
        for (Item item : items) {
            itemsToString.add(item.getId());
        }
        return itemsToString;
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
