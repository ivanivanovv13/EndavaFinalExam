package com.endava.school.supermarketapi.service;

import com.endava.school.supermarketapi.dto.PurchaseDto;
import com.endava.school.supermarketapi.dto.PurchaseDtoResponse;
import com.endava.school.supermarketapi.model.PurchasePage;
import com.endava.school.supermarketapi.model.PurchaseSearchCriteria;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;

public interface PurchaseService {
    PurchaseDtoResponse buyItemsFromSupermarket(PurchaseDto purchaseDto);

    List<PurchaseDto> getAll();

    void writePurchaseToCSV() throws IOException;

    Page<PurchaseDto> findAllFileteredPurchases(PurchasePage purchasePage, PurchaseSearchCriteria purchaseSearchCriteria);
}
