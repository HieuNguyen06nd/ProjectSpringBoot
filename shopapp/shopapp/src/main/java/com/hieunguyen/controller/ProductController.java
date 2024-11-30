package com.hieunguyen.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.util.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.github.javafaker.Faker;
import com.hieunguyen.dto.ProductDTO;
import com.hieunguyen.dto.ProductImageDTO;
import com.hieunguyen.exception.DataNotFoundException;
import com.hieunguyen.exception.InvalidException;
import com.hieunguyen.model.ProductEntity;
import com.hieunguyen.model.ProductImageEntity;
import com.hieunguyen.reponse.ProductListResponse;
import com.hieunguyen.reponse.ProductResponse;
import com.hieunguyen.service.ProductService;
import com.hieunguyen.service.impl.IProductService;

import ch.qos.logback.core.util.StringUtil;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;


@RestController
@RequestMapping("${api.prefix}/product")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class ProductController {

	IProductService iProductService;
	
	@GetMapping("")
	public ResponseEntity<ProductListResponse> getAllProduct(
			@RequestParam("page") int page,
			@RequestParam("limit") int limit
			) {
		PageRequest pageRequest = PageRequest.of(page, limit,Sort.by("createdAt").descending());
		
		Page<ProductResponse> productPage = iProductService.getAllProduct(pageRequest);
		
		int totalPage = productPage.getTotalPages();
		 
	    List<ProductResponse>productEntities = productPage.getContent();
		return ResponseEntity.ok(ProductListResponse.builder()
				.productEntities(productEntities)
				.totalPages(totalPage)
				.build());
	}
	@PostMapping("")
	public ResponseEntity<?> CearteProduct(@Valid @RequestBody ProductDTO productDto,
			BindingResult result) {
		try {
			if(result.hasErrors()) { 
				List<String> errorMessages = result.getFieldErrors()
						.stream()
						.map(FieldError::getDefaultMessage)
						.toList();
				return ResponseEntity.badRequest().body(errorMessages);
			}
			
			ProductEntity productEntity = iProductService.createProduct(productDto);
			
			return ResponseEntity.ok(productEntity);
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
		
	}
	
	@PostMapping(value = "uploads/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<?> uploadImage(
	        @PathVariable("id") Long productId,
	        @RequestParam("files") MultipartFile[] files) throws DataNotFoundException, IOException, InvalidException {

	    try {
	        ProductEntity productEntity = iProductService.getProductById(productId);

	        files = files == null ? new MultipartFile[0] : files;
	        
	        if (files.length > ProductImageEntity.MAXIMUM_IMAGE_PRODUCT) {
	            return ResponseEntity.badRequest().body("You can upload a maximum of 5 images.");
	        }

	        List<ProductImageEntity> productImageEntities = new ArrayList<>();
	        for (MultipartFile file : files) {
	            if (file.getSize() == 0) {
	                continue;
	            }
	            if (file.getSize() > 10 * 1024 * 1024) {
	                return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
	                        .body("One or more files are too large (limit is 10MB).");
	            }
	            String contentType = file.getContentType();
	            if (contentType == null || !contentType.startsWith("image/")) {
	                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                        .body("One or more files are not images.");
	            }
	            String filename = storeFile(file);
	            ProductImageEntity imageEntity = iProductService.createProductImage(productEntity.getId(),
	                    ProductImageDTO.builder()
	                            .imageUrl(filename)
	                            .build());
	            productImageEntities.add(imageEntity);
	        }

	        return ResponseEntity.ok().body(productImageEntities);

	    } catch (DataNotFoundException e) {
	        return ResponseEntity.badRequest().body(e.getMessage());
	    }
	}

	
	private String storeFile (MultipartFile file) throws IOException{
		 
		if(!isImageFile(file) && file.getOriginalFilename() == null ) {
			throw new IOException("Invalid image format ");
		}
		
		String filename = StringUtils.cleanPath(file.getOriginalFilename());
		
//		them uuid vao ten file 
		String uniqueFilename = UUID.randomUUID().toString()+"_"+filename;
//		duong dan muon luu
		Path uploadDir = Paths.get("uploads");
//		kiem tra taoj thu muc neu k ton tai
		if(!Files.exists(uploadDir)) {
			Files.createDirectories(uploadDir);
		}
//		duong dan den file
		Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
//		sao chep file vao thu mua dich
		Files.copy(file.getInputStream(), destination , StandardCopyOption.REPLACE_EXISTING);
		
		return uniqueFilename;
	}
	
	private boolean isImageFile(MultipartFile file) {
		String contentType = file.getContentType();
		return contentType != null && contentType.startsWith("image/");
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getProductByID(@PathVariable("id") Long productId) {
		try {
			ProductEntity productEntity = iProductService.getProductById(productId);
			return ResponseEntity.ok(ProductResponse.fromProduct(productEntity));
		}catch (Exception e){
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateProduct(@PathVariable("id") Long id,@RequestBody ProductDTO productDTO) {

		try {
			ProductEntity productEntity = iProductService.updateProduct(id, productDTO);
			return ResponseEntity.ok(productEntity);
		} catch (DataNotFoundException e) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable("id")  Long productId) {
		try {
			iProductService.deleteProduct(productId);
			return ResponseEntity.ok(String.format("Product with id = %d deleted successfully", productId));
		}catch (Exception e){
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

//	@PostMapping("/generateFakeProducts")
	public ResponseEntity<String> generateFakeProducts() {
		Faker faker = new Faker();
		for (int i = 0; i < 1000; i++) {
			String productName = faker.commerce().productName();
			if (iProductService.existProduct(productName)) {
				continue;
			}
			ProductDTO productDTO = ProductDTO.builder()
					.name(productName)
					.price((float) faker.number().numberBetween(100, 1000000))
					.thumbnail(productName)
					.description(faker.lorem().sentence())
					.categoryId((long) faker.number().numberBetween(1, 3))
					.build();
			try {
				iProductService.createProduct(productDTO);
			} catch (DataNotFoundException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}
		return ResponseEntity.ok("Fake product successfully generated.");
	}
	
}
