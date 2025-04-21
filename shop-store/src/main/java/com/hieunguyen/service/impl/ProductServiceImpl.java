package com.hieunguyen.service.impl;

import com.hieunguyen.dto.request.ProductItemRequest;
import com.hieunguyen.dto.request.ProductRequest;
import com.hieunguyen.dto.response.ProductResponse;
import com.hieunguyen.exception.ResourceNotFoundException;
import com.hieunguyen.model.*;
import com.hieunguyen.repository.*;
import com.hieunguyen.service.FileStorageService;
import com.hieunguyen.service.ProductService;
import com.hieunguyen.utils.DiscountType;
import com.hieunguyen.utils.ProductItemStatus;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;
    private final MaterialRepository materialRepository;
    private final FileStorageService fileStorageService;
    private final OrderItemRepository orderItemRepository;
    private final DiscountRepository discountRepository;


    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest productRequest) {
        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        Brand brand = brandRepository.findById(productRequest.getBrandId())
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found"));

        // Tạo Product mới với tập hợp ProductItem ban đầu rỗng
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .category(category)
                .brand(brand)
                .images(productRequest.getImages())
                .build();

        // Xử lý danh sách ProductItems: tạo và thêm vào Product thông qua helper method
        if (productRequest.getProductItems() != null) {
            for (ProductItemRequest itemReq : productRequest.getProductItems()) {
                ProductItem productItem = createOrUpdateProductItem(itemReq);
                // Sử dụng helper method trong Product để set quan hệ
                product.addProductItem(productItem);
            }
        }

        Product savedProduct = productRepository.save(product);
        return convertToProductResponse(savedProduct);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm"));

        // 1. CẬP NHẬT THÔNG TIN CƠ BẢN
        existingProduct.setName(productRequest.getName());
        existingProduct.setDescription(productRequest.getDescription());
        existingProduct.setCategory(categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Danh mục không tồn tại")));
        existingProduct.setBrand(brandRepository.findById(productRequest.getBrandId())
                .orElseThrow(() -> new ResourceNotFoundException("Thương hiệu không tồn tại")));

        // 2. XỬ LÝ ẢNH ========================================================
        // Xóa ảnh cũ (nếu có)
        if (productRequest.getDeletedImages() != null && !productRequest.getDeletedImages().isEmpty()) {
            fileStorageService.deleteImages(productRequest.getDeletedImages());
        }

        // Merge ảnh: (Ảnh cũ - ảnh xóa) + ảnh mới từ request
        Set<String> updatedImages = new HashSet<>();
        if (existingProduct.getImages() != null) {
            updatedImages.addAll(existingProduct.getImages());
            updatedImages.removeAll(productRequest.getDeletedImages()); // Loại bỏ ảnh đã xóa
        }
        if (productRequest.getImages() != null) {
            updatedImages.addAll(productRequest.getImages()); // Thêm ảnh mới
        }
        existingProduct.setImages(updatedImages);

        // 3. XỬ LÝ PRODUCT ITEMS ==============================================
        Set<ProductItem> existingItems = existingProduct.getProductItems();
        Set<Long> requestItemIds = new HashSet<>();

        if (productRequest.getProductItems() != null) {
            for (ProductItemRequest itemReq : productRequest.getProductItems()) {
                // Cập nhật item có sẵn
                if (itemReq.getId() != null) {
                    requestItemIds.add(itemReq.getId());
                    ProductItem existingItem = existingItems.stream()
                            .filter(item -> item.getId().equals(itemReq.getId()))
                            .findFirst()
                            .orElseThrow(() -> new ResourceNotFoundException("Biến thể không tồn tại"));

                    updateProductItem(existingItem, itemReq);
                }
                // Thêm item mới
                else {
                    ProductItem newItem = createProductItem(itemReq, existingProduct);
                    existingProduct.addProductItem(newItem);
                }
            }
        }

        // Đánh dấu các item không có trong request là INACTIVE
        existingItems.stream()
                .filter(item -> !requestItemIds.contains(item.getId()))
                .forEach(item -> item.setStatus(ProductItemStatus.INACTIVE));

        return convertToProductResponse(productRepository.save(existingProduct));
    }

    // Helper: Tạo mới ProductItem
    private ProductItem createProductItem(ProductItemRequest itemReq, Product product) {
        Color color = colorRepository.findById(itemReq.getColorId())
                .orElseThrow(() -> new ResourceNotFoundException("Màu không tồn tại"));
        Size size = sizeRepository.findById(itemReq.getSizeId())
                .orElseThrow(() -> new ResourceNotFoundException("Kích thước không tồn tại"));
        Material material = materialRepository.findById(itemReq.getMaterialId())
                .orElseThrow(() -> new ResourceNotFoundException("Chất liệu không tồn tại"));

        return ProductItem.builder()
                .product(product)
                .color(color)
                .size(size)
                .material(material)
                .price(itemReq.getPrice())
                .stock(itemReq.getStock())
                .sku(itemReq.getSku())
                .status(itemReq.getStatus())
                .build();
    }

    // Helper: Cập nhật ProductItem có sẵn
    private void updateProductItem(ProductItem item, ProductItemRequest itemReq) {
        item.setPrice(itemReq.getPrice());
        item.setStock(itemReq.getStock());
        item.setStatus(itemReq.getStatus());
        item.setColor(colorRepository.findById(itemReq.getColorId())
                .orElseThrow(() -> new ResourceNotFoundException("Màu không tồn tại")));
        item.setSize(sizeRepository.findById(itemReq.getSizeId())
                .orElseThrow(() -> new ResourceNotFoundException("Kích thước không tồn tại")));
        item.setMaterial(materialRepository.findById(itemReq.getMaterialId())
                .orElseThrow(() -> new ResourceNotFoundException("Chất liệu không tồn tại")));
        item.setSku(itemReq.getSku());
    }




    @Override
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return convertToProductResponse(product);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        productRepository.delete(product);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getTopSellingProducts(int limit) {
        Pageable pageable = org.springframework.data.domain.PageRequest.of(0, limit);
        List<Object[]> results = productRepository.findTopSellingProducts(pageable);

        return results.stream()
                .map(obj -> {
                    Product product = (Product) obj[0];  // lấy Product
                    return convertToProductResponse(product); // map sang ProductResponse
                })
                .collect(Collectors.toList());
    }



    @Override
    public String uploadProductImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        String storedPath = fileStorageService.storeFile(file, "products");
        String filename = storedPath.substring(storedPath.lastIndexOf("/") + 1);
        return fileStorageService.getFileUrl(filename, "products");
    }

    // 🛠 Tạo mới ProductItem từ ProductItemRequest
    private ProductItem createOrUpdateProductItem(@NotNull ProductItemRequest itemRequest) {
        Color color = itemRequest.getColorId() != null
                ? colorRepository.findById(itemRequest.getColorId())
                .orElseThrow(() -> new ResourceNotFoundException("Color not found"))
                : colorRepository.save(new Color(itemRequest.getColorName(), itemRequest.getHexCode()));

        Size size = itemRequest.getSizeId() != null
                ? sizeRepository.findById(itemRequest.getSizeId())
                .orElseThrow(() -> new ResourceNotFoundException("Size not found"))
                : sizeRepository.save(new Size(itemRequest.getSizeName()));

        Material material = itemRequest.getMaterialId() != null
                ? materialRepository.findById(itemRequest.getMaterialId())
                .orElseThrow(() -> new ResourceNotFoundException("Material not found"))
                : materialRepository.save(new Material(itemRequest.getMaterialName()));

        // Lưu ý: Không set Product tại đây, vì quan hệ sẽ được set sau khi tạo Product
        return ProductItem.builder()
                .color(color)
                .size(size)
                .material(material)
                .price(itemRequest.getPrice())
                .stock(itemRequest.getStock())
                .build();
    }

    // 📌 Chuyển đổi Product -> ProductResponse
    private ProductResponse convertToProductResponse(Product product) {
        // Lấy tất cả discount ACTIVE áp dụng cho product này
        List<Discount> applicableDiscounts = discountRepository.findActiveDiscountsByProductId(product.getId());

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())

                // Lấy category
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getName())

                // Lấy brand
                .brandId(product.getBrand().getId())
                .brandName(product.getBrand().getName())

                .images(product.getImages())

                // Xử lý danh sách ProductItems
                .productItems(product.getProductItems().stream()
                        .map(item -> {
                            double originalPrice = item.getPrice();
                            double newPrice = calculateDiscountedPrice(originalPrice, applicableDiscounts);
                            double maxDiscountPercent = getMaxDiscountPercent(applicableDiscounts);

                            Integer sold = orderItemRepository.getTotalSoldByProductItemId(item.getId());
                            int totalSold = sold != null ? sold : 0;

                            return ProductResponse.ProductItemResponse.builder()
                                    .id(item.getId())
                                    .sku(item.getSku())
                                    .price(originalPrice)
                                    .newPrice(newPrice)
                                    .maxDiscountPercent(maxDiscountPercent)
                                    .stock(item.getStock())
                                    .status(item.getStatus())

                                    // color
                                    .colorId(item.getColor().getId())
                                    .colorName(item.getColor().getName())
                                    .hexCode(item.getColor().getHexCode())

                                    // size
                                    .sizeId(item.getSize().getId())
                                    .sizeName(item.getSize().getName())

                                    // material
                                    .materialId(item.getMaterial().getId())
                                    .materialName(item.getMaterial().getName())

                                    .sold(totalSold)
                                    .build();
                        })
                        .collect(Collectors.toList()))
                .build();
    }

    private double calculateDiscountedPrice(double originalPrice, List<Discount> discounts) {
        double finalPrice = originalPrice;
        LocalDateTime now = LocalDateTime.now();

        for (Discount discount : discounts) {
            // Kiểm tra điều kiện áp dụng
            if (discount.getActive()
                    && (discount.getValidFrom() == null || discount.getValidFrom().isBefore(now))
                    && (discount.getValidTo() == null || discount.getValidTo().isAfter(now))) {

                if (discount.getType() == DiscountType.PERCENTAGE) {
                    finalPrice -= originalPrice * (discount.getValue() / 100.0);
                } else {
                    finalPrice -= discount.getValue();
                }
            }
        }

        return Math.max(finalPrice, 0.0); // Đảm bảo không âm
    }

    private double getMaxDiscountPercent(List<Discount> discounts) {
        return discounts.stream()
                .filter(d -> d.getType() == DiscountType.PERCENTAGE)
                .mapToDouble(Discount::getValue)
                .max()
                .orElse(0.0);
    }





}
