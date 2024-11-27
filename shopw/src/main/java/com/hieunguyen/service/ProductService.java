package com.hieunguyen.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.hieunguyen.dto.ProductDTO;
import com.hieunguyen.dto.ProductImageDTO;
import com.hieunguyen.exception.DataNotFoundException;
import com.hieunguyen.exception.InvalidException;
import com.hieunguyen.model.CategoryEntity;
import com.hieunguyen.model.ProductEntity;
import com.hieunguyen.model.ProductImageEntity;
import com.hieunguyen.reponse.ProductResponse;
import com.hieunguyen.repository.CategoryRepository;
import com.hieunguyen.repository.ProductImageRepository;
import com.hieunguyen.repository.ProductRepository;
import com.hieunguyen.service.impl.IProductService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService implements IProductService {

	ProductRepository productRepository;
	CategoryRepository categoryRepository;
	ProductImageRepository productImageRepository;
	
	@Override
	public ProductEntity createProduct(ProductDTO productDTO) throws DataNotFoundException {
		CategoryEntity existingCategory = categoryRepository.findById(productDTO.getCategoryId())
		.orElseThrow(()->new DataNotFoundException("Cannot find category id" + productDTO.getCategoryId()));

		ProductEntity productEntity = ProductEntity.builder()
				.name(productDTO.getName())
				.price(productDTO.getPrice())
				.thumbnail(productDTO.getThumbnail())
				.description(productDTO.getDeccription())
				.categoryEntity(existingCategory)
				.build();
		return productRepository.save(productEntity);
	}

	@Override
	public ProductEntity getProductById(long id) throws DataNotFoundException {
		
		return productRepository.findById(id).orElseThrow(()->new DataNotFoundException("cannot find product with id = "+id));
	}

	@Override
	public Page<ProductResponse> getAllProduct(PageRequest pageRequest) {
//		page - limit
		return productRepository.findAll(pageRequest).map(productEntity ->{ 
		    ProductResponse productResponse =	ProductResponse.builder()
					.name(productEntity.getName())
					.price(productEntity.getPrice())
					.thumbnail(productEntity.getThumbnail())
					.deccription(productEntity.getDescription())
					.categoryId(productEntity.getCategoryEntity().getId())
					.build();
		    productResponse.setCreatedAt(productEntity.getCreatedAt());
		    productResponse.setUpdatedAt(productEntity.getUpdatedAt());
			return productResponse;
		});
	}

	@Override
	public ProductEntity updateProduct(long id, ProductDTO productDTO) throws DataNotFoundException {
		ProductEntity productEntity = getProductById(id);
		if(productEntity != null) {
			CategoryEntity existingCategory = categoryRepository.findById(productDTO.getCategoryId())
					.orElseThrow(()->new DataNotFoundException("Cannot find category id" + productDTO.getCategoryId()));
			productEntity.setName(productDTO.getName());
			productEntity.setCategoryEntity(existingCategory);
			productEntity.setPrice(productDTO.getPrice());
			productEntity.setDescription(productDTO.getDeccription());
			productEntity.setThumbnail(productDTO.getThumbnail());
			
			return productRepository.save(productEntity);
		}
		
		return null;
	}

	@Override
	public void deleteProduct(long id) {
		Optional<ProductEntity>optionalProduct = productRepository.findById(id);
		if(optionalProduct.isPresent()) {
			productRepository.delete(optionalProduct.get());
		}	
	}

	@Override
	public boolean existProduct(String name) {
		return productRepository.existsByName(name);
	}
	
	@Override
	public ProductImageEntity createProductImage(Long id, ProductImageDTO productImageDTO) throws DataNotFoundException, InvalidException {
		
		ProductEntity existingProduct = productRepository.findById(id)
				.orElseThrow(()->new DataNotFoundException("Cannot find category id" + productImageDTO.getProductID()));
		
		ProductImageEntity productImageEntity = ProductImageEntity.builder()
				.productEntity(existingProduct)
				.imageUrl(productImageDTO.getImageUrl())
				.build();
		
		int size = productImageRepository.findByProductEntityId(id).size();
		if(size >=  ProductImageEntity.MAXIMUM_IMAGE_PRODUCT) {
			throw new InvalidException("Number of image < =  "+  ProductImageEntity.MAXIMUM_IMAGE_PRODUCT);
		}
		
		return productImageRepository.save(productImageEntity);
	}
	

}
