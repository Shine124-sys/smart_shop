package com.app.smartshop.repo;

import com.app.smartshop.model.Product;
import com.app.smartshop.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT * FROM products p WHERE SOUNDEX(p.name) = SOUNDEX(:name) OR p.name LIKE %:name%", nativeQuery = true)
    List<Product> findByNameFuzzy(@Param("name") String name);

    List<Product> findByShop(Shop shop);

    List<Product> findByShopIn(List<Shop> shops);

    @Query(value = "SELECT * FROM products p WHERE p.shop_id IN :shopIds AND (SOUNDEX(p.name) = SOUNDEX(:name) OR p.name LIKE %:name%)", nativeQuery = true)
    List<Product> findByShopIdsAndNameFuzzy(@Param("shopIds") List<Long> shopIds,
                                             @Param("name") String name);
}

