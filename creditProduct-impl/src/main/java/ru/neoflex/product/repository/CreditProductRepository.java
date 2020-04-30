package ru.neoflex.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.neoflex.product.entity.CreditProduct;

import java.util.List;
import java.util.Optional;

@Repository
public interface CreditProductRepository extends JpaRepository<CreditProduct, Integer>, JpaSpecificationExecutor {
    Optional<CreditProduct> findFirstByIdProduct(Integer id);

    @Modifying
    @Query("SELECT c FROM CreditProduct c WHERE (:price BETWEEN c.minPrice AND c.maxPrice) AND (:term BETWEEN c.minTerm AND c.maxTerm)")
    List<CreditProduct> findBetweenPriceAndTerm(Long price, Integer term);

    Optional<CreditProduct> findByProductName(String productName);
}
