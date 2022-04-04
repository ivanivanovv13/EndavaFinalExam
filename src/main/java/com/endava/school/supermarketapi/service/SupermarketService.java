package com.endava.school.supermarketapi.service;

import com.endava.school.supermarketapi.dto.SupermarketDto;
import com.endava.school.supermarketapi.dto.SupermarketDtoResponse;
import com.endava.school.supermarketapi.dto.addItemsToSupermarketDto;
import com.endava.school.supermarketapi.dto.addItemsToSupermarketDtoResponse;

public interface SupermarketService {
    SupermarketDto addSupermarket(SupermarketDto supermarketDto);

    addItemsToSupermarketDtoResponse addItemsToSupermarket(addItemsToSupermarketDto addItemsToSupermarketDto);

    SupermarketDtoResponse getSupermarketById(String supermarketId);
}
