package com.endava.school.supermarketapi.service.impl;

import com.endava.school.supermarketapi.dto.ItemDto;
import com.endava.school.supermarketapi.model.Item;
import com.endava.school.supermarketapi.repository.ItemRepository;
import com.endava.school.supermarketapi.service.ItemService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
        this.modelMapper = new ModelMapper();
    }

    @Override
    public ItemDto addItem(ItemDto itemDto) {
        Item itemToSave = modelMapper.map(itemDto, Item.class);
        Item newItem =itemRepository.save(itemToSave);
        return modelMapper.map(newItem,ItemDto.class);
    }
}
