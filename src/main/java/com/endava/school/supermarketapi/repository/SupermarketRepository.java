package com.endava.school.supermarketapi.repository;

import com.endava.school.supermarketapi.model.Supermarket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SupermarketRepository extends JpaRepository<Supermarket,String> {
}
