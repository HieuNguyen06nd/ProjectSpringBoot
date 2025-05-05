// DiscountServiceImpl.java
package com.hieunguyen.shopstorev2.service.impl;

import com.hieunguyen.shopstorev2.dto.request.DiscountRequest;
import com.hieunguyen.shopstorev2.dto.response.DiscountResponse;
import com.hieunguyen.shopstorev2.dto.response.DiscountProductRuleResponse;
import com.hieunguyen.shopstorev2.dto.request.DiscountProductRuleRequest;
import com.hieunguyen.shopstorev2.entities.Discount;
import com.hieunguyen.shopstorev2.entities.DiscountProductRule;
import com.hieunguyen.shopstorev2.entities.ProductItem;
import com.hieunguyen.shopstorev2.repository.DiscountRepository;
import com.hieunguyen.shopstorev2.repository.ProductItemRepository;
import com.hieunguyen.shopstorev2.service.DiscountService;
import com.hieunguyen.shopstorev2.utils.DiscountStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRepository discountRepository;
    private final ProductItemRepository productItemRepository;

    @Override
    @Transactional
    public DiscountResponse create(DiscountRequest request) {
        Discount discount = mapFromRequest(request);
        updateStatus(discount);

        if (request.getProductRules() != null) {
            List<DiscountProductRule> rules = request.getProductRules().stream().map(rule -> {
                ProductItem item = productItemRepository.findById(rule.getProductItemId()).orElseThrow();
                return DiscountProductRule.builder()
                        .discount(discount)
                        .productItem(item)
                        .percentage(rule.getPercentage())
                        .fixedAmount(rule.getFixedAmount())
                        .build();
            }).collect(Collectors.toList());
            discount.setProductRules(rules);
        }

        return mapToResponse(discountRepository.save(discount));
    }

    @Override
    @Transactional
    public DiscountResponse update(Long id, DiscountRequest request) {
        Discount discount = discountRepository.findById(id).orElseThrow();
        updateFields(discount, request);
        updateStatus(discount);

        discount.getProductRules().clear();
        if (request.getProductRules() != null) {
            List<DiscountProductRule> rules = request.getProductRules().stream().map(rule -> {
                ProductItem item = productItemRepository.findById(rule.getProductItemId()).orElseThrow();
                return DiscountProductRule.builder()
                        .discount(discount)
                        .productItem(item)
                        .percentage(rule.getPercentage())
                        .fixedAmount(rule.getFixedAmount())
                        .build();
            }).toList();
            discount.getProductRules().addAll(rules);
        }

        return mapToResponse(discountRepository.save(discount));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        discountRepository.deleteById(id);
    }

    @Override
    public DiscountResponse getById(Long id) {
        Discount discount = discountRepository.findById(id).orElseThrow();
        updateStatus(discount);
        return mapToResponse(discount);
    }

    @Override
    public DiscountResponse getByCode(String code) {
        Discount discount = discountRepository.findByCode(code).orElseThrow();
        updateStatus(discount);
        return mapToResponse(discount);
    }

    @Override
    public List<DiscountResponse> getAll() {
        return discountRepository.findAll().stream()
                .peek(this::updateStatus)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @Scheduled(fixedRate = 3600000)
    public void updateStatuses() {
        List<Discount> all = discountRepository.findAll();
        for (Discount d : all) {
            updateStatus(d);
        }
        discountRepository.saveAll(all);
    }

    private void updateFields(Discount d, DiscountRequest r) {
        d.setCode(r.getCode());
        d.setName(r.getName());
        d.setDescription(r.getDescription());
        d.setApplicableTo(r.getApplicableTo());
        d.setDiscountType(r.getDiscountType());
        d.setPercentage(r.getPercentage());
        d.setFixedAmount(r.getFixedAmount());
        d.setMinOrderAmount(r.getMinOrderAmount());
        d.setMaxDiscountAmount(r.getMaxDiscountAmount());
        d.setValidFrom(r.getValidFrom());
        d.setValidTo(r.getValidTo());
        d.setPriority(r.getPriority());
    }

    private void updateStatus(Discount d) {
        LocalDateTime now = LocalDateTime.now();
        if (d.getValidFrom() != null && now.isBefore(d.getValidFrom())) {
            d.setStatus(DiscountStatus.UPCOMING);
        } else if (d.getValidTo() != null && now.isAfter(d.getValidTo())) {
            d.setStatus(DiscountStatus.EXPIRED);
        } else {
            d.setStatus(DiscountStatus.ACTIVE);
        }
    }

    private BigDecimal calculateDiscountedPrice(ProductItem item) {
        List<DiscountProductRule> rules = discountRepository
                .findActiveRulesByProductItem(item.getId(), LocalDateTime.now());

        for (DiscountProductRule rule : rules) {
            if (rule.getPercentage() != null) {
                return item.getPrice().subtract(
                        item.getPrice().multiply(rule.getPercentage()).divide(BigDecimal.valueOf(100))
                );
            }
            if (rule.getFixedAmount() != null) {
                return item.getPrice().subtract(rule.getFixedAmount());
            }
        }
        return item.getPrice();
    }

    private Discount mapFromRequest(DiscountRequest r) {
        return Discount.builder()
                .code(r.getCode())
                .name(r.getName())
                .description(r.getDescription())
                .applicableTo(r.getApplicableTo())
                .discountType(r.getDiscountType())
                .percentage(r.getPercentage())
                .fixedAmount(r.getFixedAmount())
                .minOrderAmount(r.getMinOrderAmount())
                .maxDiscountAmount(r.getMaxDiscountAmount())
                .validFrom(r.getValidFrom())
                .validTo(r.getValidTo())
                .priority(r.getPriority())
                .build();
    }

    private DiscountResponse mapToResponse(Discount d) {
        return DiscountResponse.builder()
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
                .status(d.getStatus())
                .productRules(d.getProductRules().stream().map(p -> DiscountProductRuleResponse.builder()
                        .id(p.getId())
                        .productItemId(p.getProductItem().getId())
                        .percentage(p.getPercentage())
                        .fixedAmount(p.getFixedAmount())
                        .build()).toList())
                .build();
    }
}