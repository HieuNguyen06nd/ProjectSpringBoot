package com.hieunguyen.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hieunguyen.dto.CategoryDTO;
import com.hieunguyen.dto.OrderDTO;
import com.hieunguyen.dto.OrderDetailDTO;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("${api.prefix}/orderdetails")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class OrderDetailController {
	@PostMapping("")
	public  ResponseEntity<?> CreateOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO,
			BindingResult result) {
		if(result.hasErrors()) {
			List<String>errorMessages = result.getFieldErrors()
					.stream()
					.map(FieldError::getDefaultMessage)
					.toList();
			return ResponseEntity.badRequest().body(errorMessages);
		}
		return ResponseEntity.ok("hello post"+orderDetailDTO);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getOrderDetail(@Valid @PathVariable("id") Long id) {
		try {
			return ResponseEntity.ok("get");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
//	ds order detal 1 order
	@GetMapping("/order/{order_id}")
	public ResponseEntity<?> getOrderDetails(@Valid @PathVariable("order_id") Long order_id) {
		try {
			return ResponseEntity.ok("get" +order_id);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateOrderDetail(@Valid @PathVariable Long id,
			@RequestBody OrderDetailDTO orderDetailDTO) {
		//TODO: process PUT request
		
		return ResponseEntity.ok("put order");
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteOrderDetail(@Valid @PathVariable Long id) {
		//TODO: process PUT request
		
		return ResponseEntity.ok("delete order");
	}
	
	
}
