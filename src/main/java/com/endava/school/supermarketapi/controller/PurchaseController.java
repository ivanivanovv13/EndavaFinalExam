package com.endava.school.supermarketapi.controller;

import com.endava.school.supermarketapi.dto.PurchaseDto;
import com.endava.school.supermarketapi.dto.PurchaseDtoResponse;
import com.endava.school.supermarketapi.exception.MoneyAreNotEnoughException;
import com.endava.school.supermarketapi.model.PurchasePage;
import com.endava.school.supermarketapi.model.PurchaseSearchCriteria;
import com.endava.school.supermarketapi.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.endava.school.supermarketapi.common.ExceptionMessages.YOU_NEED_TO_PAY;

@RestController
@RequestMapping("/purchases")
public class PurchaseController {
    private PurchaseService purchaseService;

    @Autowired
    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping("/buy-items-from-supermarket")
    public ResponseEntity<PurchaseDtoResponse> buyItemsFromSupermarket(@RequestBody PurchaseDto purchaseDto) {
        if (purchaseDto.getPaymentType().toString().equals("CASH") && Objects.isNull(purchaseDto.getCashAmount())) {
            throw new MoneyAreNotEnoughException(YOU_NEED_TO_PAY);
        }
        return new ResponseEntity<>(purchaseService.buyItemsFromSupermarket(purchaseDto), HttpStatus.OK);
    }

    @GetMapping("/get-all-purchases")
    public ResponseEntity<List<PurchaseDto>> getAllPurchase() {
        return new ResponseEntity<>(purchaseService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/get-all-filtered")
    public ResponseEntity<Page<PurchaseDto>> findAll(PurchasePage purchasePage, PurchaseSearchCriteria purchaseSearchCriteria) {
        return ResponseEntity.ok(purchaseService.findAllFileteredPurchases(purchasePage, purchaseSearchCriteria));
    }

    @GetMapping("/csv")
    public ResponseEntity getAllPurchaseInCsv() throws IOException {
        this.purchaseService.writePurchaseToCSV();
        return ResponseEntity.ok("The file is exported successfully");
    }
}
