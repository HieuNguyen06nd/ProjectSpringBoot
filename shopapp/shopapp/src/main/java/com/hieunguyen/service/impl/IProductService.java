package com.hieunguyen.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.hieunguyen.dto.ProductDTO;
import com.hieunguyen.dto.ProductImageDTO;
import com.hieunguyen.exception.DataNotFoundException;
import com.hieunguyen.exception.InvalidException;
import com.hieunguyen.model.ProductEntity;
import com.hieunguyen.model.ProductImageEntity;
import com.hieunguyen.reponse.ProductResponse;

@Service
public interface IProductService {

	ProductEntity createProduct(ProductDTO productDTO) throws DataNotFoundException;
	
	ProductEntity getProductById(long id) throws DataNotFoundException;
	
	Page<ProductResponse> getAllProduct( PageRequest pageRequest);
	
	ProductEntity updateProduct( long id, ProductDTO productDTO) throws DataNotFoundException;
	
	void deleteProduct(long id);
	
	boolean existProduct( String name);
	
	ProductImageEntity createProductImage(Long id, ProductImageDTO productImageDTO) throws DataNotFoundException, InvalidException;
	
}
