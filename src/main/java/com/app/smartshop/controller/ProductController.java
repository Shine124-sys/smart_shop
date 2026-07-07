package com.app.smartshop.controller;

import com.app.smartshop.model.NearbyProductResult;
import com.app.smartshop.model.Product;
import com.app.smartshop.model.Shop;
import com.app.smartshop.model.User;
import com.app.smartshop.service.ProductService;
import com.app.smartshop.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ShopService shopService;

    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody Product product, @AuthenticationPrincipal User user) {
        Optional<Shop> shop = shopService.getShopByOwner(user);
        if (shop.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You must create a shop before adding products!");
        }

        product.setShop(shop.get());
        return ResponseEntity.ok(productService.addProduct(product));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String name) {
        return ResponseEntity.ok(productService.searchProductsByName(name));
    }

    @GetMapping("/my-products")
    public ResponseEntity<?> getMyProducts(@AuthenticationPrincipal User user) {
        Optional<Shop> shop = shopService.getShopByOwner(user);
        if (shop.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("You don't have a shop yet.");
        }

        return ResponseEntity.ok(productService.getProductsByShop(shop.get()));
    }

    /**
     * Search products sold by shops near the given coordinates.
     *
     * @param lat      user latitude
     * @param lng      user longitude
     * @param radius   search radius in km (default 5)
     * @param name     optional product name filter
     */
    @GetMapping("/nearby")
    public ResponseEntity<List<NearbyProductResult>> searchNearby(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(defaultValue = "5") double radius,
            @RequestParam(required = false) String name) {
        return ResponseEntity.ok(productService.searchNearby(lat, lng, radius, name));
    }
}

