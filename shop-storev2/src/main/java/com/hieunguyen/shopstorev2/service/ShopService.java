package com.hieunguyen.shopstorev2.service;

import com.hieunguyen.shopstorev2.dto.request.ShopRequest;
import com.hieunguyen.shopstorev2.dto.response.ProductResponse;
import com.hieunguyen.shopstorev2.dto.response.ShopResponse;

import java.util.List;

public interface ShopService {
    ShopResponse createShop(ShopRequest request);
    ShopResponse getMyShop();
    ShopResponse updateMyShop(ShopRequest request);

}