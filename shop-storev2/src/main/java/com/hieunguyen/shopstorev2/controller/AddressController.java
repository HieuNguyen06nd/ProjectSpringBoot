package com.hieunguyen.shopstorev2.controller;

import com.hieunguyen.shopstorev2.dto.request.AddressRequest;
import com.hieunguyen.shopstorev2.dto.response.AddressResponse;
import com.hieunguyen.shopstorev2.dto.response.ApiResponse;
import com.hieunguyen.shopstorev2.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ApiResponse<AddressResponse> addAddress(@RequestBody AddressRequest request) {
        return ApiResponse.success(addressService.addAddress(request));
    }

    @GetMapping
    public ApiResponse<List<AddressResponse>> getUserAddresses() {
        return ApiResponse.success(addressService.getUserAddresses());
    }

    @PutMapping("/{id}")
    public ApiResponse<AddressResponse> updateAddress(@PathVariable Long id, @RequestBody @Valid AddressRequest request) {
        return ApiResponse.success(addressService.updateAddress(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return ApiResponse.success("Xóa địa chỉ thành công");
    }
}