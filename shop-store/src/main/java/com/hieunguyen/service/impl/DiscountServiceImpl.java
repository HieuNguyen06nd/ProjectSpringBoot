package com.hieunguyen.service.impl;

import com.hieunguyen.dto.request.DiscountRequest;
import com.hieunguyen.dto.response.DiscountResponse;
import com.hieunguyen.exception.ResourceNotFoundException;
import com.hieunguyen.model.Discount;
import com.hieunguyen.repository.DiscountRepository;
import com.hieunguyen.service.DiscountService;
import com.hieunguyen.utils.ApplicableType;
import com.hieunguyen.utils.DiscountType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRepository discountRepository;

    @Override
    public DiscountResponse createDiscount(DiscountRequest request) {
        validateDiscountRequest(request);
        Discount newDiscount = convertToEntity(request);
        Discount savedDiscount = discountRepository.save(newDiscount);
        return convertToResponse(savedDiscount);
    }

    @Override
    public DiscountResponse updateDiscount(Long id, DiscountRequest request) {
        Discount existingDiscount = discountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Discount not found with id: " + id));

        validateDiscountRequest(request);
        updateEntityFromRequest(existingDiscount, request);

        Discount updatedDiscount = discountRepository.save(existingDiscount);
        return convertToResponse(updatedDiscount);
    }

    @Override
    public List<DiscountResponse> getAllDiscounts() {
        return discountRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DiscountResponse getDiscountById(Long id) {
        return discountRepository.findById(id)
                .map(this::convertToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Discount not found with id: " + id));
    }

    @Override
    public void toggleDiscountStatus(Long id) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Discount not found with id: " + id));

        discount.setActive(!discount.getActive());
        discountRepository.save(discount);
    }

    @Override
    public List<DiscountResponse> getActiveProductDiscounts(Long productId) {
        return discountRepository.findActiveProductDiscounts(productId, LocalDateTime.now())
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DiscountResponse> getActiveOrderDiscounts() {
        return discountRepository.findActiveOrderDiscounts(LocalDateTime.now())
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Helper methods
    private Discount convertToEntity(DiscountRequest request) {
        return Discount.builder()
                .code(request.getCode())
                .name(request.getName())
                .description(request.getDescription())
                .value(request.getValue())
                .type(request.getType())
                .validFrom(request.getValidFrom())
                .validTo(request.getValidTo())
                .applicableTo(request.getApplicableTo())
                .applicableProductIds(request.getApplicableProductIds())
                .minOrderAmount(request.getMinOrderAmount())
                .active(true)
                .build();
    }

    private void updateEntityFromRequest(Discount discount, DiscountRequest request) {
        discount.setCode(request.getCode());
        discount.setName(request.getName());
        discount.setDescription(request.getDescription());
        discount.setValue(request.getValue());
        discount.setType(request.getType());
        discount.setValidFrom(request.getValidFrom());
        discount.setValidTo(request.getValidTo());
        discount.setApplicableTo(request.getApplicableTo());
        discount.setApplicableProductIds(request.getApplicableProductIds());
        discount.setMinOrderAmount(request.getMinOrderAmount());
    }

    private DiscountResponse convertToResponse(Discount discount) {
        return DiscountResponse.builder()
                .id(discount.getId())
                .code(discount.getCode())
                .name(discount.getName())
                .description(discount.getDescription())
                .value(discount.getValue())
                .type(discount.getType())
                .validFrom(discount.getValidFrom())
                .validTo(discount.getValidTo())
                .applicableTo(discount.getApplicableTo())
                .applicableProductIds(discount.getApplicableProductIds())
                .minOrderAmount(discount.getMinOrderAmount())
                .isActive(discount.getActive())
                .createdAt(discount.getCreatedAt())
                .build();
    }

    private void validateDiscountRequest(DiscountRequest request) {
        // Validate date range
        if (request.getValidTo().isBefore(request.getValidFrom())) {
            throw new IllegalArgumentException("End date must be after start date");
        }

        // Validate product discounts
        if (request.getApplicableTo() == ApplicableType.PRODUCT &&
                (request.getApplicableProductIds() == null || request.getApplicableProductIds().isEmpty())) {
            throw new IllegalArgumentException("At least one product must be selected for product discounts");
        }

        // Validate fixed amount
        if (request.getType() == DiscountType.FIXED_AMOUNT && request.getValue() <= 0) {
            throw new IllegalArgumentException("Fixed amount must be greater than 0");
        }

        // Validate percentage range
        if (request.getType() == DiscountType.PERCENTAGE &&
                (request.getValue() <= 0 || request.getValue() > 100)) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100");
        }
    }
}