package com.hieunguyen.dto.request;

import jakarta.mail.search.SearchTerm;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Data
public class ProductRequest {

    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String name;

    private String description;

    @NotNull(message = "Danh mục sản phẩm không được để trống")
    private Long categoryId;

    @NotNull(message = "Thương hiệu sản phẩm không được để trống")
    private Long brandId;

    private List<@NotNull(message = "Danh sách sản phẩm con không được null") ProductItemRequest> productItems;

    private Set<String> images;
    private Set<String> newImages;
    private Set<String> deletedImages;

}
