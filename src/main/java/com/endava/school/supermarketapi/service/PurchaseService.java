package com.endava.school.supermarketapi.service;

import com.endava.school.supermarketapi.dto.PurchaseDto;
import com.endava.school.supermarketapi.dto.PurchaseDtoResponse;
import com.endava.school.supermarketapi.model.PurchasePage;
import com.endava.school.supermarketapi.model.PurchaseSearchCriteria;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public interface PurchaseService {
    PurchaseDtoResponse buyItemsFromSupermarket(PurchaseDto purchaseDto);

    List<PurchaseDtoResponse> getAllPurchases();

    void writePurchaseToCSV(Writer writer) throws IOException;

    Page<PurchaseDtoResponse> findAllFilteredPurchases(PurchasePage purchasePage, PurchaseSearchCriteria purchaseSearchCriteria);
}
