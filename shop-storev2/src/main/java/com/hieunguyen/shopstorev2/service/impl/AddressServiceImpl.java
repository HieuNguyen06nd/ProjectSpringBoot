package com.hieunguyen.shopstorev2.service.impl;

import com.hieunguyen.shopstorev2.dto.request.AddressRequest;
import com.hieunguyen.shopstorev2.dto.response.AddressResponse;
import com.hieunguyen.shopstorev2.entities.Address;
import com.hieunguyen.shopstorev2.entities.User;
import com.hieunguyen.shopstorev2.repository.AddressRepository;
import com.hieunguyen.shopstorev2.repository.UserRepository;
import com.hieunguyen.shopstorev2.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Override
    public AddressResponse addAddress(AddressRequest request) {
        User user = getCurrentUser();

        if (Boolean.TRUE.equals(request.getIsDefault())) {
            addressRepository.findByUser(user).forEach(addr -> {
                addr.setIsDefault(false);
                addressRepository.save(addr);
            });
        }

        Address address = Address.builder()
                .user(user)
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .detail(request.getDetail())
                .city(request.getCity())
                .district(request.getDistrict())
                .ward(request.getWard())
                .isDefault(request.getIsDefault() != null ? request.getIsDefault() : false)
                .build();

        addressRepository.save(address);

        return mapToResponse(address);
    }

    @Override
    public List<AddressResponse> getUserAddresses() {
        User user = getCurrentUser();
        return addressRepository.findByUser(user).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AddressResponse updateAddress(Long id, AddressRequest request) {
        User user = getCurrentUser();
        Address address = addressRepository.findById(id)
                .filter(a -> a.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (Boolean.TRUE.equals(request.getIsDefault())) {
            addressRepository.findByUser(user).forEach(addr -> {
                addr.setIsDefault(false);
                addressRepository.save(addr);
            });
        }

        address.setFullName(request.getFullName());
        address.setPhone(request.getPhone());
        address.setDetail(request.getDetail());
        address.setCity(request.getCity());
        address.setDistrict(request.getDistrict());
        address.setWard(request.getWard());
        address.setIsDefault(request.getIsDefault() != null ? request.getIsDefault() : false);

        addressRepository.save(address);
        return mapToResponse(address);
    }

    @Override
    public void deleteAddress(Long id) {
        User user = getCurrentUser();
        Address address = addressRepository.findById(id)
                .filter(a -> a.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Address not found"));
        addressRepository.delete(address);
    }

    private AddressResponse mapToResponse(Address address) {
        return AddressResponse.builder()
                .id(address.getId())
                .fullName(address.getFullName())
                .phone(address.getPhone())
                .detail(address.getDetail())
                .city(address.getCity())
                .district(address.getDistrict())
                .ward(address.getWard())
                .isDefault(address.getIsDefault())
                .build();
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}