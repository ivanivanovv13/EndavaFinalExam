package com.endava.school.supermarketapi.service.impl;

import com.endava.school.supermarketapi.common.enums.ItemType;
import com.endava.school.supermarketapi.dto.ItemDto;
import com.endava.school.supermarketapi.model.Item;
import com.endava.school.supermarketapi.repository.ItemRepository;
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

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@RunWith(MockitoJUnitRunner.class)
class ItemServiceImplTest {
    @Mock
    ItemRepository itemRepository;
    @Mock
    ModelMapper modelMapper;
    @InjectMocks
    ItemServiceImpl underTest;
    ItemDto itemDto = new ItemDto("Donut", 0.89, "FOOD");
    Item item = new Item(UUID.randomUUID().toString(), "Donut", 0.89, ItemType.FOOD);

    @BeforeEach
    public void setUp() {
        Mockito.when(modelMapper.map(itemDto, Item.class)).thenReturn(item);
        Mockito.when(modelMapper.map(item, ItemDto.class)).thenReturn(itemDto);
    }

    @Test
    void ifItemIsAdded() {
        Mockito.when(itemRepository.save(Mockito.any())).thenReturn(item);
        ItemDto newItem = underTest.addItem(itemDto);
        assertThat(itemDto.getName()).isEqualTo(newItem.getName());
        assertThat(itemDto.getItemType()).isEqualTo(newItem.getItemType());
        assertThat(itemDto.getPrice()).isEqualTo(newItem.getPrice());
    }
}