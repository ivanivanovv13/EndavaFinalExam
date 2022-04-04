package com.endava.school.supermarketapi.controller;

import com.endava.school.supermarketapi.dto.SupermarketDto;
import com.endava.school.supermarketapi.dto.SupermarketDtoResponse;
import com.endava.school.supermarketapi.dto.addItemsToSupermarketDto;
import com.endava.school.supermarketapi.dto.addItemsToSupermarketDtoResponse;
import com.endava.school.supermarketapi.service.SupermarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/supermarkets")
public class SupermarketController {
    private SupermarketService supermarketService;

    @Autowired
    public SupermarketController(SupermarketService supermarketService) {
        this.supermarketService = supermarketService;
    }

    @PostMapping("/create-supermarket")
    public ResponseEntity<SupermarketDto> createSupermarket(@RequestBody SupermarketDto supermarketDto) {
        return new ResponseEntity<>(supermarketService.addSupermarket(supermarketDto), HttpStatus.CREATED);
    }

    @PostMapping("/add-items-to-supermarket")
    public ResponseEntity<addItemsToSupermarketDtoResponse> addItemsToSupermarket(@RequestBody addItemsToSupermarketDto addItemsToSupermarketDto) {
        return new ResponseEntity<>(supermarketService.addItemsToSupermarket(addItemsToSupermarketDto), HttpStatus.OK);
    }

    @GetMapping("/get-supermarket-by-id{supermarketId}")
    public ResponseEntity<SupermarketDtoResponse> getSupermarketById(@PathVariable(required = true, value = "supermarketId") String supermarketId) {
        return new ResponseEntity<>(supermarketService.getSupermarketById(supermarketId), HttpStatus.OK);
    }
}
