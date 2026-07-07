package com.app.smartshop.controller;

import com.app.smartshop.model.Shop;
import com.app.smartshop.model.User;
import com.app.smartshop.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/shops")
public class ShopController {

    @Autowired
    private ShopService shopService;

    @PostMapping("/create")
    public ResponseEntity<?> createShop(@RequestBody Shop shop, @AuthenticationPrincipal User user) {
        if (shopService.getShopByOwner(user).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You already have a shop.");
        }

        shop.setOwner(user);
        return ResponseEntity.ok(shopService.createShop(shop));
    }

    @GetMapping("/view")
    public List<Shop> viewShops() {
        return shopService.getAllShops();
    }

    @GetMapping("/view/{id}")
    public Shop viewShopById(@PathVariable Long id) {
        return shopService.getShopById(id).orElse(null);
    }

    @GetMapping("/my-shop")
    public ResponseEntity<?> viewMyShop(@AuthenticationPrincipal User user) {
        Optional<Shop> shop = shopService.getShopByOwner(user);
        if (shop.isPresent()) {
            return ResponseEntity.ok(shop.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("You don't have a shop yet.");
    }
}
