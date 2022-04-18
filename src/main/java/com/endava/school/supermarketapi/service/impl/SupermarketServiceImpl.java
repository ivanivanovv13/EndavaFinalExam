package com.endava.school.supermarketapi.service.impl;

import com.endava.school.supermarketapi.dto.*;
import com.endava.school.supermarketapi.exception.ItemNotFoundException;
import com.endava.school.supermarketapi.exception.SupermarketNotFoundException;
import com.endava.school.supermarketapi.model.Item;
import com.endava.school.supermarketapi.model.Supermarket;
import com.endava.school.supermarketapi.repository.ItemRepository;
import com.endava.school.supermarketapi.repository.SupermarketRepository;
import com.endava.school.supermarketapi.service.SupermarketService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.endava.school.supermarketapi.common.ExceptionMessages.ITEM_NOT_FOUND;
import static com.endava.school.supermarketapi.common.ExceptionMessages.SUPERMARKET_NOT_FOUND;

@Service
public class SupermarketServiceImpl implements SupermarketService {
    private final SupermarketRepository supermarketRepository;
    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public SupermarketServiceImpl(SupermarketRepository supermarketRepository, ItemRepository itemRepository) {
        this.supermarketRepository = supermarketRepository;
        this.itemRepository = itemRepository;
        this.modelMapper = new ModelMapper();
    }

    @Override
    public SupermarketDto addSupermarket(SupermarketDto supermarketDto) {
        Supermarket supermarket = modelMapper.map(supermarketDto, Supermarket.class);
        Supermarket newSupermarket = supermarketRepository.save(supermarket);
        return modelMapper.map(newSupermarket, SupermarketDto.class);
    }

    @Override
    public addItemsToSupermarketDtoResponse addItemsToSupermarket(addItemsToSupermarketDto addItemsToSupermarketDto) {
        Supermarket supermarket = supermarketRepository.findById(addItemsToSupermarketDto.getSupermarketId()).orElseThrow(() -> new SupermarketNotFoundException(SUPERMARKET_NOT_FOUND));
        List<Item> items = findItemsByIdFromList(addItemsToSupermarketDto.getItemIds());
        supermarket.setItems(items);
        Supermarket savedSupermarket = supermarketRepository.save(supermarket);
        return new addItemsToSupermarketDtoResponse(savedSupermarket.getId(), getItemsNamesFromList(supermarket.getItems()));
    }

    @Override
    public SupermarketDtoResponse getSupermarketById(String supermarketId) {
        Supermarket supermarket = supermarketRepository.findById(supermarketId).orElseThrow(() -> new SupermarketNotFoundException(SUPERMARKET_NOT_FOUND));
        List<ItemDto> itemsDto = mapItemsToItemsDto(supermarket.getItems());
        return new SupermarketDtoResponse(supermarket.getName(), supermarket.getAddress(), supermarket.getPhoneNumber(), supermarket.getWorkHours(), itemsDto);
    }

    private List<Item> findItemsByIdFromList(List<String> listOfItemsIds) {
        List<Item> items = new ArrayList<>();
        for (String itemId : listOfItemsIds) {
            Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException(ITEM_NOT_FOUND));
            items.add(item);
        }
        return items;
    }

    private List<ItemDto> mapItemsToItemsDto(List<Item> items) {
        List<ItemDto> itemsDto = new ArrayList();
        for (Item item : items) {
            itemsDto.add(modelMapper.map(item, ItemDto.class));
        }
        return itemsDto;
    }

    private List<String> getItemsNamesFromList(List<Item> listOfItems) {
        List<String> itemsNames = new ArrayList<>();
        for (Item item : listOfItems) {
            itemsNames.add(item.getName());
        }
        return itemsNames;
    }
}
