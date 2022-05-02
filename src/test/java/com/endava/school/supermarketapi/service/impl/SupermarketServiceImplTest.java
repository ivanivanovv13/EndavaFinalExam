package com.endava.school.supermarketapi.service.impl;

import com.endava.school.supermarketapi.common.enums.ItemType;
import com.endava.school.supermarketapi.dto.SupermarketDto;
import com.endava.school.supermarketapi.dto.SupermarketDtoResponse;
import com.endava.school.supermarketapi.dto.addItemsToSupermarketDto;
import com.endava.school.supermarketapi.dto.addItemsToSupermarketDtoResponse;
import com.endava.school.supermarketapi.exception.ItemNotFoundException;
import com.endava.school.supermarketapi.exception.SupermarketNotFoundException;
import com.endava.school.supermarketapi.model.Item;
import com.endava.school.supermarketapi.model.Supermarket;
import com.endava.school.supermarketapi.repository.ItemRepository;
import com.endava.school.supermarketapi.repository.SupermarketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.endava.school.supermarketapi.common.ExceptionMessages.ITEM_NOT_FOUND;
import static com.endava.school.supermarketapi.common.ExceptionMessages.SUPERMARKET_NOT_FOUND;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@RunWith(MockitoJUnitRunner.class)
class SupermarketServiceImplTest {

    @Mock
    private SupermarketRepository supermarketRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private SupermarketServiceImpl underTest;
    private SupermarketDto supermarketDto;
    private Supermarket supermarket;
    private List<String> itemIds;
    private List<Item> items;
    private Item itemOne;
    private Item itemTwo;

    @BeforeEach
    public void setUp() {
        supermarketDto = new SupermarketDto("Fantastico", "Sofia,Veliko Turnovo,73", "0882421474", "08:00-22:00");
        items = new ArrayList<>();
        itemIds = new ArrayList<>();
        itemOne = new Item(UUID.randomUUID().toString(), "Donut", 0.89, ItemType.FOOD);
        itemTwo = new Item(UUID.randomUUID().toString(), "Coca Cola", 1.45, ItemType.DRINKS);
        items.add(itemOne);
        items.add(itemTwo);
        supermarket = new Supermarket(UUID.randomUUID().toString(), "Fantastico", "Sofia,Veliko Turnovo,73", "0882421474", "08:00-22:00", items);
        Mockito.when(modelMapper.map(supermarketDto, Supermarket.class)).thenReturn(supermarket);
        Mockito.when(modelMapper.map(supermarket, SupermarketDto.class)).thenReturn(supermarketDto);
    }

    @Test
    void addSupermarket() {
        Mockito.when(supermarketRepository.save(Mockito.any())).thenReturn(supermarket);
        SupermarketDto newSupermarket = underTest.addSupermarket(supermarketDto);
        assertThat(newSupermarket.getName()).isEqualTo(supermarketDto.getName());
        assertThat(newSupermarket.getAddress()).isEqualTo(supermarketDto.getAddress());
        assertThat(newSupermarket.getPhoneNumber()).isEqualTo(supermarketDto.getPhoneNumber());
        assertThat(newSupermarket.getWorkHours()).isEqualTo(supermarketDto.getWorkHours());
    }

    @Test
    void addItemsToSupermarketShouldAddItems() {
        Mockito.when(supermarketRepository.findById(supermarket.getId())).thenReturn(Optional.of(supermarket));
        Mockito.when(itemRepository.findById(itemOne.getId())).thenReturn(Optional.of(itemOne));
        Mockito.when(itemRepository.findById(itemTwo.getId())).thenReturn(Optional.of(itemTwo));
        Mockito.when(supermarketRepository.save(supermarket)).thenReturn(supermarket);
        itemIds.add(itemOne.getId());
        itemIds.add(itemTwo.getId());
        addItemsToSupermarketDtoResponse addItemsToSupermarketDtoResponse = underTest.addItemsToSupermarket(new addItemsToSupermarketDto(supermarket.getId(), itemIds));
        assertThat(addItemsToSupermarketDtoResponse.getItemsNames().size()).isEqualTo(itemIds.size());
    }

    @Test
    void addItemsToSupermarketThrowException() {
        Mockito.when(supermarketRepository.findById(supermarket.getId())).thenReturn(Optional.of(supermarket));
        Mockito.when(itemRepository.findById(itemOne.getId())).thenThrow(new ItemNotFoundException(ITEM_NOT_FOUND));
        itemIds.add(itemOne.getId());
        itemIds.add(itemTwo.getId());
        Throwable exception = assertThrows(ItemNotFoundException.class, () -> underTest.addItemsToSupermarket(new addItemsToSupermarketDto(supermarket.getId(), itemIds)));
        assertThat(ITEM_NOT_FOUND).isEqualTo(exception.getMessage());
    }

    @Test
    void getSupermarketByIdShouldReturnSupermarket() {
        Mockito.when(supermarketRepository.findById(supermarket.getId())).thenReturn(Optional.of(supermarket));
        SupermarketDtoResponse foundSupermarket = underTest.getSupermarketById(supermarket.getId());
        assertThat(foundSupermarket.getName()).isEqualTo(supermarket.getName());
        assertThat(foundSupermarket.getAddress()).isEqualTo(supermarket.getAddress());
        assertThat(foundSupermarket.getPhoneNumber()).isEqualTo(supermarket.getPhoneNumber());
        assertThat(foundSupermarket.getWorkHours()).isEqualTo(supermarket.getWorkHours());
    }

    @Test
    void getSupermarketByIdShouldThrowException() {
        Mockito.when(supermarketRepository.findById(supermarket.getId())).thenThrow(new SupermarketNotFoundException(SUPERMARKET_NOT_FOUND));
        Throwable exception = assertThrows(SupermarketNotFoundException.class, () -> underTest.getSupermarketById(supermarket.getId()));
        assertThat(SUPERMARKET_NOT_FOUND).isEqualTo(exception.getMessage());
    }
}