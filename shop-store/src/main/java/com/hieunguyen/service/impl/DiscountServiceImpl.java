package com.hieunguyen.service.impl;

import com.hieunguyen.dto.request.DiscountRequest;
import com.hieunguyen.dto.request.ProductRuleDTO;
import com.hieunguyen.dto.response.DiscountResponse;
import com.hieunguyen.model.Discount;
import com.hieunguyen.model.DiscountProductRule;
import com.hieunguyen.model.ProductItem;
import com.hieunguyen.repository.DiscountRepository;
import com.hieunguyen.repository.ProductItemRepository;
import com.hieunguyen.service.DiscountService;
import com.hieunguyen.utils.ApplicableType;
import com.hieunguyen.utils.DiscountStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {
    private final DiscountRepository discountRepo;
    private final ProductItemRepository productRepo;

    /**
     * Tạo mới mã giảm giá
     */
    @Transactional
    public DiscountResponse createDiscount(DiscountRequest req) {
        // 1. Map DTO -> Entity
        Discount d = Discount.builder()
                .code(req.getCode())
                .name(req.getName())
                .description(req.getDescription())
                .applicableTo(req.getApplicableTo())
                .discountType(req.getDiscountType())
                .percentage(req.getPercentage())
                .fixedAmount(req.getFixedAmount())
                .minOrderAmount(req.getMinOrderAmount())
                .maxDiscountAmount(req.getMaxDiscountAmount())
                .validFrom(req.getValidFrom())
                .validTo(req.getValidTo())
                .priority(req.getPriority())
                .status(DiscountStatus.ACTIVE)
                .build();

        // 2. Xử lý PRODUCT-level rules
        if (req.getApplicableTo() == ApplicableType.PRODUCT && req.getProductRules() != null) {
            for (ProductRuleDTO dto : req.getProductRules()) {
                ProductItem pi = productRepo.findById(dto.getProductItemId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid productItemId"));

                DiscountProductRule rule = DiscountProductRule.builder()
                        .discount(d)
                        .productItem(pi)
                        .percentage(dto.getPercentage())
                        .fixedAmount(dto.getFixedAmount())
                        .build();

                d.getProductRules().add(rule);
            }
        }

        // 3. Lưu xuống DB
        Discount saved = discountRepo.save(d);

        // 4. Map Entity -> DTO Response
        return mapToResponse(saved);
    }

    /**
     * Chuyển Discount entity -> DiscountResponse
     */
    public DiscountResponse mapToResponse(Discount d) {
        DiscountResponse.DiscountResponseBuilder rb = DiscountResponse.builder()
                .id(d.getId())
                .code(d.getCode())
                .name(d.getName())
                .description(d.getDescription())
                .applicableTo(d.getApplicableTo())
                .discountType(d.getDiscountType())
                .percentage(d.getPercentage())
                .fixedAmount(d.getFixedAmount())
                .minOrderAmount(d.getMinOrderAmount())
                .maxDiscountAmount(d.getMaxDiscountAmount())
                .validFrom(d.getValidFrom())
                .validTo(d.getValidTo())
                .priority(d.getPriority())
                .status(d.getStatus());

        if (d.getApplicableTo() == ApplicableType.PRODUCT) {
            List<ProductRuleDTO> rules = d.getProductRules().stream()
                    .map(r -> {
                        ProductRuleDTO dto = new ProductRuleDTO();
                        dto.setProductItemId(r.getProductItem().getId());
                        dto.setPercentage(r.getPercentage());
                        dto.setFixedAmount(r.getFixedAmount());
                        return dto;
                    }).collect(Collectors.toList());
            rb.productRules(rules);
        }

        return rb.build();
    }
}