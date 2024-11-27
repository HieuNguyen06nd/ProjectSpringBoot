package com.hieunguyen.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hieunguyen.dto.CategoryDTO;
import com.hieunguyen.dto.OrderDTO;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("${api.prefix}/order")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class OrderController {

	@PostMapping("")
	public  ResponseEntity<?> CreateOrder(@Valid @RequestBody OrderDTO orderDTO,
			BindingResult result) {
		try {
			if(result.hasErrors()) {
				List<String>errorMessages = result.getFieldErrors()
						.stream()
						.map(FieldError::getDefaultMessage)
						.toList();
				return ResponseEntity.badRequest().body(errorMessages);
			}
			return ResponseEntity.ok("hello order post"+orderDTO);
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	
	@GetMapping("/{user_id}")
	public ResponseEntity<?> getOrder(@Valid @PathVariable("user_id") Long user_id) {
		try {
			return ResponseEntity.ok("get");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateOrder(@Valid @PathVariable Long id,
			@RequestBody OrderDTO orderDTO) {
		//TODO: process PUT request
		
		return ResponseEntity.ok("put order");
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteOrder(@Valid @PathVariable Long id) {
		//TODO: process PUT request
		
		return ResponseEntity.ok("delete order");
	}
	
	
	
}
