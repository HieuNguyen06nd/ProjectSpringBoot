
package com.hieunguyen.shopstorev2.service.impl;

import com.hieunguyen.shopstorev2.dto.request.ProductRequest;
import com.hieunguyen.shopstorev2.dto.response.ProductItemResponse;
import com.hieunguyen.shopstorev2.dto.response.ProductResponse;
import com.hieunguyen.shopstorev2.entities.*;
import com.hieunguyen.shopstorev2.repository.*;
import com.hieunguyen.shopstorev2.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final UserRepository userRepository;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;
    private final MaterialRepository materialRepository;
    private final ProductItemRepository productItemRepository;
    private final DiscountRepository discountRepository;

    @Override
    public ProductResponse create(ProductRequest req) {
        User user = getCurrentUser();
        Shop shop = shopRepository.findByOwner(user).orElseThrow();

        Category category = categoryRepository.findById(req.getCategoryId()).orElseThrow();
        Brand brand = brandRepository.findById(req.getBrandId()).orElseThrow();

        Product product = Product.builder()
                .shop(shop)
                .name(req.getName())
                .description(req.getDescription())
                .category(category)
                .brand(brand)
                .images(req.getImages())
                .build();

        Set<ProductItem> items = req.getItems().stream().map(itemReq -> {
            String sku = (itemReq.getSku() != null && !itemReq.getSku().isBlank()) ? itemReq.getSku() : generateSku();
            if (productItemRepository.existsBySku(sku)) throw new RuntimeException("SKU đã tồn tại: " + sku);
            return ProductItem.builder()
                    .product(product)
                    .color(colorRepository.findById(itemReq.getColorId()).orElseThrow())
                    .size(sizeRepository.findById(itemReq.getSizeId()).orElseThrow())
                    .material(materialRepository.findById(itemReq.getMaterialId()).orElseThrow())
                    .sku(sku)
                    .price(itemReq.getPrice())
                    .stock(itemReq.getStock())
                    .status(itemReq.getStatus())
                    .build();
        }).collect(Collectors.toSet());

        product.setProductItems(items);
        productRepository.save(product);

        return map(product);
    }

    @Override
    public List<ProductResponse> getMyShopProducts() {
        User user = getCurrentUser();
        Shop shop = shopRepository.findByOwner(user).orElseThrow();
        return productRepository.findAllByShop(shop).stream().map(this::map).collect(Collectors.toList());
    }

    @Override
    public ProductResponse updateProduct(Long productId, ProductRequest req) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
        if (product.isDeleted()) throw new RuntimeException("Không thể cập nhật sản phẩm đã xoá");

        product.setName(req.getName());
        product.setDescription(req.getDescription());
        product.setImages(req.getImages());
        product.setCategory(categoryRepository.findById(req.getCategoryId()).orElseThrow());
        product.setBrand(brandRepository.findById(req.getBrandId()).orElseThrow());

        product.getProductItems().clear();
        Set<ProductItem> updatedItems = req.getItems().stream().map(itemReq -> {
            String sku = (itemReq.getSku() != null && !itemReq.getSku().isBlank()) ? itemReq.getSku() : generateSku();
            if (productItemRepository.existsBySku(sku)) throw new RuntimeException("SKU đã tồn tại: " + sku);

            return ProductItem.builder()
                    .product(product)
                    .color(colorRepository.findById(itemReq.getColorId()).orElseThrow())
                    .size(sizeRepository.findById(itemReq.getSizeId()).orElseThrow())
                    .material(materialRepository.findById(itemReq.getMaterialId()).orElseThrow())
                    .sku(sku)
                    .price(itemReq.getPrice())
                    .stock(itemReq.getStock())
                    .status(itemReq.getStatus())
                    .build();
        }).collect(Collectors.toSet());
        product.setProductItems(updatedItems);

        productRepository.save(product);
        return map(product);
    }

    @Override
    public void updateStock(Long itemId, Integer stock) {
        ProductItem item = productItemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Không tìm thấy item"));
        item.setStock(stock);
        productItemRepository.save(item);
    }

    private ProductResponse map(Product p) {
        return ProductResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .images(p.getImages())
                .categoryName(p.getCategory().getName())
                .brandName(p.getBrand().getName())
                .createdAt(p.getCreatedAt())
                .items(p.getProductItems().stream().map(item -> ProductItemResponse.builder()
                        .id(item.getId())
                        .sku(item.getSku())
                        .price(item.getPrice())
                        .discountedPrice(calculateDiscountedPrice(item))
                        .stock(item.getStock())
                        .colorName(item.getColor().getName())
                        .sizeName(item.getSize().getName())
                        .materialName(item.getMaterial().getName())
                        .status(item.getStatus())
                        .build()).collect(Collectors.toList()))
                .build();
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

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow();
    }

    private String generateSku() {
        String date = java.time.LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        String random = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6).toUpperCase();
        return "SP-" + date + "-" + random;
    }
}
