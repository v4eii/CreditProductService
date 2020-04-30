package ru.neoflex.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.neoflex.product.entity.CreditProduct;
import ru.neoflex.product.repository.CreditProductRepository;

import java.util.List;

@Service
public class CreditProductServiceImpl implements CreditProductService{

    @Autowired
    private CreditProductRepository creditProductRepository;

    @Override
    public List<CreditProduct> findSuitableCreditProduct(Long price, Integer term) {
        return creditProductRepository.findBetweenPriceAndTerm(price, term);
    }
}
