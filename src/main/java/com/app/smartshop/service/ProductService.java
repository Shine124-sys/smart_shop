package com.app.smartshop.service;

import com.app.smartshop.model.NearbyProductResult;
import com.app.smartshop.model.Product;
import com.app.smartshop.model.Shop;
import com.app.smartshop.repo.ProductRepository;
import com.app.smartshop.repo.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ShopRepository shopRepository;

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameFuzzy(name);
    }

    public List<Product> getProductsByShop(Shop shop) {
        return productRepository.findByShop(shop);
    }

    /**
     * Search products in shops within {@code radiusKm} km of (lat, lng).
     * Optionally filter by product name (fuzzy). Results sorted by distance.
     *
     * @param lat       user latitude
     * @param lng       user longitude
     * @param radiusKm  search radius in kilometres
     * @param name      optional product name filter (null or blank = all products)
     */
    public List<NearbyProductResult> searchNearby(double lat, double lng,
                                                   double radiusKm, String name) {
        // 1. Find nearby shops
        List<Shop> nearbyShops = shopRepository.findShopsNearby(lat, lng, radiusKm);
        if (nearbyShops.isEmpty()) return List.of();

        List<Long> shopIds = nearbyShops.stream().map(Shop::getId).toList();

        // 2. Fetch products (filtered by name if provided)
        List<Product> products;
        if (name != null && !name.isBlank()) {
            products = productRepository.findByShopIdsAndNameFuzzy(shopIds, name);
        } else {
            products = productRepository.findByShopIn(nearbyShops);
        }

        // 3. Build result DTOs with distance, sorted nearest-first
        return products.stream()
                .map(p -> {
                    Shop shop = p.getShop();
                    double dist = haversineKm(lat, lng, shop.getLatitude(), shop.getLongitude());
                    return new NearbyProductResult(p, shop, dist);
                })
                .sorted(Comparator.comparingDouble(NearbyProductResult::getDistanceKm))
                .collect(Collectors.toList());
    }

    // ── Haversine distance ────────────────────────────────────────────────────

    private double haversineKm(double lat1, double lng1, double lat2, double lng2) {
        final double R = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                 + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                 * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}

