package com.endava.school.supermarketapi.service;

import com.endava.school.supermarketapi.dto.ItemDto;
import com.endava.school.supermarketapi.model.Item;

import java.util.Optional;

public interface ItemService {
    ItemDto addItem(ItemDto itemDto);
}
