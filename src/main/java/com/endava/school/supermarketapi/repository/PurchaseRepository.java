package com.endava.school.supermarketapi.repository;

import com.endava.school.supermarketapi.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase,String> {
}
