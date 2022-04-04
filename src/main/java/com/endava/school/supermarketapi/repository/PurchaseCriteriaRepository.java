package com.endava.school.supermarketapi.repository;

import com.endava.school.supermarketapi.dto.PurchaseDto;
import com.endava.school.supermarketapi.model.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class PurchaseCriteriaRepository {

    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    public PurchaseCriteriaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<Purchase> findAllWithFilters(PurchasePage purchasePage, PurchaseSearchCriteria purchaseSearchCriteria) {

        CriteriaQuery<Purchase> criteriaQuery = criteriaBuilder.createQuery(Purchase.class);
        Root<Purchase> purchaseRoot = criteriaQuery.from(Purchase.class);
        Predicate predicate = getPredicate(purchaseSearchCriteria, purchaseRoot);
        criteriaQuery.where(predicate);
        setOrder(purchasePage, criteriaQuery, purchaseRoot);
        TypedQuery<Purchase> typedQuery = entityManager.createQuery(criteriaQuery);
        int totalRows = typedQuery.getResultList().size();
        typedQuery.setFirstResult(purchasePage.getPageNumber() * purchasePage.getPageSize());
        typedQuery.setMaxResults(purchasePage.getPageSize());

        Pageable pageable = getPageable(purchasePage);

        return new PageImpl<>(typedQuery.getResultList(), pageable, totalRows);
    }

    private Predicate getPredicate(PurchaseSearchCriteria purchaseSearchCriteria, Root<Purchase> purchaseRoot) {
        List<Predicate> predicates = new ArrayList<>();
        if (Objects.nonNull(purchaseSearchCriteria.getPayment_type())) {
            predicates.add(criteriaBuilder.like(purchaseRoot.get("paymentType"), "%" + purchaseSearchCriteria.getPayment_type() + "%"));
        }
        if (Objects.nonNull(purchaseSearchCriteria.getCash_amount())) {
            predicates.add(criteriaBuilder.equal(purchaseRoot.get("cashAmount"), purchaseSearchCriteria.getCash_amount()));
        }
        if (Objects.nonNull(purchaseSearchCriteria.getItem_id())) {
            Join<Purchase, Item> joinBook = purchaseRoot.joinList("items");
            predicates.add(criteriaBuilder.like(joinBook.get("items"), "%" + purchaseSearchCriteria.getItem_id() + "%"));
        }
        if (Objects.nonNull(purchaseSearchCriteria.getSupermarket_id())) {
            Join<Purchase, Supermarket> joinBook = purchaseRoot.joinList("supermarket");
            predicates.add(criteriaBuilder.like(joinBook.get("supermarket"), "%" + purchaseSearchCriteria.getSupermarket_id() + "%"));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void setOrder(PurchasePage purchasePage, CriteriaQuery<Purchase> criteriaQuery, Root<Purchase> purchaseRoot) {
        if (purchasePage.getSortDirection().equals(Sort.Direction.ASC)) {
            criteriaQuery.orderBy(criteriaBuilder.asc(purchaseRoot.get(purchasePage.getSortBy())));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(purchaseRoot.get(purchasePage.getSortBy())));
        }
    }

    private Pageable getPageable(PurchasePage purchasePage) {
        Sort sort = Sort.by(purchasePage.getSortDirection(), purchasePage.getSortBy());
        return PageRequest.of(purchasePage.getPageNumber(), purchasePage.getPageSize(), sort);
    }
}
