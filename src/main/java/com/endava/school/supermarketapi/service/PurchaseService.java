package com.endava.school.supermarketapi.service;

import com.endava.school.supermarketapi.dto.PurchaseDto;
import com.endava.school.supermarketapi.dto.PurchaseDtoResponse;

import java.util.List;

public interface PurchaseService {
    PurchaseDtoResponse buyItemsFromSupermarket(PurchaseDto purchaseDto);
    List<PurchaseDto> getAll();
}
