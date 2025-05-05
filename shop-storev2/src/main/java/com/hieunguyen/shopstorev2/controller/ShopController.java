
package com.hieunguyen.shopstorev2.controller;

import com.hieunguyen.shopstorev2.dto.request.ShopRequest;
import com.hieunguyen.shopstorev2.dto.response.ApiResponse;
import com.hieunguyen.shopstorev2.dto.response.ProductResponse;
import com.hieunguyen.shopstorev2.dto.response.ShopResponse;
import com.hieunguyen.shopstorev2.service.FileStorageService;
import com.hieunguyen.shopstorev2.service.ShopService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/shop")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;
    private final FileStorageService fileStorageService;

    @PostMapping
    public ApiResponse<ShopResponse> createShop(@RequestBody @Valid ShopRequest request) {
        return ApiResponse.success(shopService.createShop(request));
    }

    @GetMapping
    public ApiResponse<ShopResponse> getMyShop() {
        return ApiResponse.success(shopService.getMyShop());
    }

    @PutMapping
    public ApiResponse<ShopResponse> updateMyShop(@RequestBody @Valid ShopRequest request) {
        return ApiResponse.success(shopService.updateMyShop(request));
    }

    @PostMapping("/upload-logo")
    public ApiResponse<ShopResponse> uploadShopLogo(@RequestParam("file") MultipartFile file) {
        String filePath = fileStorageService.saveFile(file, "shop-logos");
        ShopRequest update = new ShopRequest();
        update.setLogoUrl(filePath);
        return ApiResponse.success(shopService.updateMyShop(update));
    }

}
