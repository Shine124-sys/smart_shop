package com.app.smartshop.model;

/**
 * DTO returned for nearby-product search results.
 * Carries product details plus the owning shop name and distance.
 */
public class NearbyProductResult {

    private Long productId;
    private String productName;
    private String productDescription;
    private Double productPrice;
    private String category;

    private Long shopId;
    private String shopName;
    private String shopLocation;
    private Double shopLatitude;
    private Double shopLongitude;
    private Double distanceKm;

    public NearbyProductResult(Product product, Shop shop, double distanceKm) {
        this.productId = product.getId();
        this.productName = product.getName();
        this.productDescription = product.getDescription();
        this.productPrice = product.getPrice();
        this.category = product.getCategory();

        this.shopId = shop.getId();
        this.shopName = shop.getName();
        this.shopLocation = shop.getLocation();
        this.shopLatitude = shop.getLatitude();
        this.shopLongitude = shop.getLongitude();
        this.distanceKm = Math.round(distanceKm * 100.0) / 100.0;
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public Long getProductId() { return productId; }
    public String getProductName() { return productName; }
    public String getProductDescription() { return productDescription; }
    public Double getProductPrice() { return productPrice; }
    public String getCategory() { return category; }

    public Long getShopId() { return shopId; }
    public String getShopName() { return shopName; }
    public String getShopLocation() { return shopLocation; }
    public Double getShopLatitude() { return shopLatitude; }
    public Double getShopLongitude() { return shopLongitude; }
    public Double getDistanceKm() { return distanceKm; }
}
