package com.endava.school.supermarketapi.repository;

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
        if (Objects.nonNull(purchaseSearchCriteria.getPaymentType())) {
            predicates.add(criteriaBuilder.equal(purchaseRoot.get("paymentType"), purchaseSearchCriteria.getPaymentType()));
        }
        if (purchaseSearchCriteria.getTotalCashAmount() != 0) {
            predicates.add(criteriaBuilder.equal(purchaseRoot.get("totalCashAmount"), purchaseSearchCriteria.getTotalCashAmount()));
        }
        if (Objects.nonNull(purchaseSearchCriteria.getItemId())) {
            Join<Purchase, Item> joinItem = purchaseRoot.joinList("items");
            predicates.add(criteriaBuilder.equal(joinItem.get("id"), purchaseSearchCriteria.getItemId()));
        }
        if (Objects.nonNull(purchaseSearchCriteria.getItemName())) {
            Join<Purchase, Item> joinItem = purchaseRoot.joinList("items");
            predicates.add(criteriaBuilder.like(joinItem.get("name"), "%" + purchaseSearchCriteria.getItemName() + "%"));
        }
        if (purchaseSearchCriteria.getItemPrice() != 0) {
            Join<Purchase, Item> joinItem = purchaseRoot.joinList("items");
            predicates.add(criteriaBuilder.equal(joinItem.get("price"), purchaseSearchCriteria.getItemPrice()));
        }
        if (Objects.nonNull(purchaseSearchCriteria.getItemType())) {
            Join<Purchase, Item> joinItem = purchaseRoot.joinList("items");
            predicates.add(criteriaBuilder.equal(joinItem.get("itemType"), purchaseSearchCriteria.getItemType()));
        }
        if (Objects.nonNull(purchaseSearchCriteria.getSupermarketId())) {
            Join<Purchase, Supermarket> joinSupermarket = purchaseRoot.join("supermarket");
            predicates.add(criteriaBuilder.equal(joinSupermarket.get("id"), purchaseSearchCriteria.getSupermarketId()));
        }
        if (Objects.nonNull(purchaseSearchCriteria.getSupermarketName())) {
            Join<Purchase, Supermarket> joinSupermarket = purchaseRoot.join("supermarket");
            predicates.add(criteriaBuilder.equal(joinSupermarket.get("name"), purchaseSearchCriteria.getSupermarketName()));
        }
        if (Objects.nonNull(purchaseSearchCriteria.getSupermarketPhoneNumber())) {
            Join<Purchase, Supermarket> joinSupermarket = purchaseRoot.join("supermarket");
            predicates.add(criteriaBuilder.equal(joinSupermarket.get("phoneNumber"), purchaseSearchCriteria.getSupermarketPhoneNumber()));
        }
        if (Objects.nonNull(purchaseSearchCriteria.getSupermarketAddress())) {
            Join<Purchase, Supermarket> joinSupermarket = purchaseRoot.join("supermarket");
            predicates.add(criteriaBuilder.equal(joinSupermarket.get("address"), purchaseSearchCriteria.getSupermarketAddress()));
        }
        if (Objects.nonNull(purchaseSearchCriteria.getSupermarketName())) {
            Join<Purchase, Supermarket> joinSupermarket = purchaseRoot.join("supermarket");
            predicates.add(criteriaBuilder.equal(joinSupermarket.get("workHours"), purchaseSearchCriteria.getSupermarketWorkHours()));
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
