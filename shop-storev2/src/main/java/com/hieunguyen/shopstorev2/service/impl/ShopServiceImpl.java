package com.hieunguyen.shopstorev2.service.impl;

import com.hieunguyen.shopstorev2.dto.request.ShopRequest;
import com.hieunguyen.shopstorev2.dto.response.ShopResponse;
import com.hieunguyen.shopstorev2.entities.Shop;
import com.hieunguyen.shopstorev2.entities.User;
import com.hieunguyen.shopstorev2.repository.ShopRepository;
import com.hieunguyen.shopstorev2.repository.UserRepository;
import com.hieunguyen.shopstorev2.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {

    private final ShopRepository shopRepository;
    private final UserRepository userRepository;

    @Override
    public ShopResponse createShop(ShopRequest request) {
        User user = getCurrentUser();

        if (shopRepository.existsByOwner(user)) {
            throw new RuntimeException("User đã có shop.");
        }

        Shop shop = Shop.builder()
                .owner(user)
                .name(request.getName())
                .description(request.getDescription())
                .logoUrl(request.getLogoUrl())
                .build();

        shopRepository.save(shop);

        return mapToResponse(shop);
    }

    @Override
    public ShopResponse getMyShop() {
        User user = getCurrentUser();
        Shop shop = shopRepository.findByOwner(user)
                .orElseThrow(() -> new RuntimeException("Shop không tồn tại."));
        return mapToResponse(shop);
    }

    @Override
    public ShopResponse updateMyShop(ShopRequest request) {
        User user = getCurrentUser();
        Shop shop = shopRepository.findByOwner(user)
                .orElseThrow(() -> new RuntimeException("Shop không tồn tại."));

        if (request.getName() != null) shop.setName(request.getName());
        if (request.getDescription() != null) shop.setDescription(request.getDescription());
        if (request.getLogoUrl() != null) shop.setLogoUrl(request.getLogoUrl());

        shopRepository.save(shop);
        return mapToResponse(shop);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow();
    }

    private ShopResponse mapToResponse(Shop shop) {
        return ShopResponse.builder()
                .id(shop.getId())
                .name(shop.getName())
                .description(shop.getDescription())
                .logoUrl(shop.getLogoUrl())
                .status(shop.getStatus())
                .deleted(shop.isDeleted())
                .build();
    }
}
