package com.hieunguyen.controller;

import com.hieunguyen.dto.response.ResponseData;
import com.hieunguyen.exception.ErrorCode;
import com.hieunguyen.model.Address;
import com.hieunguyen.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    // Tạo địa chỉ mới cho một user
    // Ví dụ: POST /api/addresses/user/1
    @PostMapping("/user/{userId}")
    public ResponseEntity<ResponseData<Address>> createAddress(@PathVariable Long userId,
                                                               @RequestBody Address address) {
        Address savedAddress = addressService.createAddress(userId, address);
        return ResponseEntity.ok(new ResponseData<>(200, "Địa chỉ được tạo thành công", savedAddress));
    }

    // Lấy địa chỉ theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<Address>> getAddressById(@PathVariable Long id) {
        Address address = addressService.getAddressById(id);
        if (address == null) {
            return ResponseEntity.ok(new ResponseData<>(ErrorCode.NOT_FOUND.getCode(), "Địa chỉ không tồn tại", null));
        }
        return ResponseEntity.ok(new ResponseData<>(200, "Địa chỉ được tìm thấy", address));
    }

    // Lấy tất cả địa chỉ của một user
    // Ví dụ: GET /api/addresses/user/1
    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseData<List<Address>>> getAddressesByUserId(@PathVariable Long userId) {
        List<Address> addresses = addressService.getAddressesByUserId(userId);
        return ResponseEntity.ok(new ResponseData<>(200, "Danh sách địa chỉ", addresses));
    }

    // Lấy địa chỉ mặc định của một user
    // Ví dụ: GET /api/addresses/user/1/default
    @GetMapping("/user/{userId}/default")
    public ResponseEntity<ResponseData<Address>> getDefaultAddress(@PathVariable Long userId) {
        Address address = addressService.getDefaultAddress(userId);
        if (address == null) {
            return ResponseEntity.ok(new ResponseData<>(ErrorCode.NOT_FOUND.getCode(), "Địa chỉ mặc định không tồn tại", null));
        }
        return ResponseEntity.ok(new ResponseData<>(200, "Địa chỉ mặc định được tìm thấy", address));
    }

    // Cập nhật địa chỉ theo ID
    @PutMapping("/{id}")
    public ResponseEntity<ResponseData<Address>> updateAddress(@PathVariable Long id,
                                                               @RequestBody Address address) {
        Address updatedAddress = addressService.updateAddress(id, address);
        if (updatedAddress == null) {
            return ResponseEntity.ok(new ResponseData<>(ErrorCode.NOT_FOUND.getCode(), "Địa chỉ không tồn tại", null));
        }
        return ResponseEntity.ok(new ResponseData<>(200, "Địa chỉ được cập nhật thành công", updatedAddress));
    }

    // Xoá địa chỉ theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<Void>> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return ResponseEntity.ok(new ResponseData<>(200, "Địa chỉ đã được xóa", null));
    }
}
