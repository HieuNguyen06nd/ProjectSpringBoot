package com.hieunguyen.controller;

import com.hieunguyen.config.CustomUserDetails;
import com.hieunguyen.dto.response.AddressResponse;
import com.hieunguyen.dto.response.ResponseData;
import com.hieunguyen.exception.BusinessException;
import com.hieunguyen.exception.ErrorCode;
import com.hieunguyen.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseData<AddressResponse> createAddress(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestBody com.hieunguyen.model.Address address
    ) {
        AddressResponse saved = addressService.createAddress(currentUser.getId(), address);
        return new ResponseData<>(200, "Địa chỉ được tạo thành công", saved);
    }

    @GetMapping("/{id}")
    public ResponseData<AddressResponse> getAddressById(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long id
    ) {
        AddressResponse addr = addressService.getAddressById(id);
        if (!addr.getUser().getId().equals(currentUser.getId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "Không có quyền truy cập địa chỉ này");
        }
        return new ResponseData<>(200, "Địa chỉ được tìm thấy", addr);
    }

    @GetMapping
    public ResponseData<List<AddressResponse>> getAllAddresses(
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        List<AddressResponse> list = addressService.getAddressesByUserId(currentUser.getId());
        return new ResponseData<>(200, "Danh sách địa chỉ", list);
    }

    @GetMapping("/default")
    public ResponseData<AddressResponse> getDefaultAddress(
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        AddressResponse def = addressService.getDefaultAddress(currentUser.getId());
        if (!def.getUser().getId().equals(currentUser.getId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "Không có quyền truy cập địa chỉ mặc định");
        }
        return new ResponseData<>(200, "Địa chỉ mặc định được tìm thấy", def);
    }

    @PutMapping("/{id}")
    public ResponseData<AddressResponse> updateAddress(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long id,
            @RequestBody com.hieunguyen.model.Address address
    ) {
        AddressResponse existing = addressService.getAddressById(id);
        if (!existing.getUser().getId().equals(currentUser.getId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "Không có quyền cập nhật địa chỉ này");
        }
        AddressResponse updated = addressService.updateAddress(id, address);
        return new ResponseData<>(200, "Địa chỉ được cập nhật thành công", updated);
    }

    @DeleteMapping("/{id}")
    public ResponseData<Void> deleteAddress(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long id
    ) {
        AddressResponse existing = addressService.getAddressById(id);
        if (!existing.getUser().getId().equals(currentUser.getId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "Không có quyền xóa địa chỉ này");
        }
        addressService.deleteAddress(id);
        return new ResponseData<>(200, "Địa chỉ đã được xóa", null);
    }
}