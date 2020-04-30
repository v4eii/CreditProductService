package ru.neoflex.product.service;

import ru.neoflex.product.entity.CreditProduct;

import java.util.List;

public interface CreditProductService {
    List<CreditProduct> findSuitableCreditProduct(Long price, Integer term);
}
