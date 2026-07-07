package com.app.smartshop.repo;

import com.app.smartshop.model.Shop;
import com.app.smartshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    Optional<Shop> findByOwner(User owner);

    /**
     * Find shops within {@code radiusKm} kilometres of (lat, lng)
     * using the Haversine formula.
     */
    @Query(value = """
            SELECT * FROM shops s
            WHERE s.latitude IS NOT NULL AND s.longitude IS NOT NULL
              AND (6371 * ACOS(
                    COS(RADIANS(:lat)) * COS(RADIANS(s.latitude))
                    * COS(RADIANS(s.longitude) - RADIANS(:lng))
                    + SIN(RADIANS(:lat)) * SIN(RADIANS(s.latitude))
                  )) <= :radiusKm
            """, nativeQuery = true)
    List<Shop> findShopsNearby(@Param("lat") double lat,
                               @Param("lng") double lng,
                               @Param("radiusKm") double radiusKm);
}
