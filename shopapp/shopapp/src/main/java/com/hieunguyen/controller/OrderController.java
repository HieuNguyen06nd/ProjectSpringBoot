package com.hieunguyen.controller;

import java.util.List;

import com.hieunguyen.model.OrderEntity;
import com.hieunguyen.reponse.OrderReponse;
import com.hieunguyen.service.impl.IOrderService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class OrderController {

	IOrderService iOrderService;

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
			OrderEntity orderEntity = iOrderService.createOrder(orderDTO);
			return ResponseEntity.ok(orderEntity);
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	
	@GetMapping("user/{user_id}")
	public ResponseEntity<?> getOrders(@Valid @PathVariable("user_id") Long user_id) {
		try {
			List<OrderEntity> orderEntityList = iOrderService.findByUserId(user_id);
			return ResponseEntity.ok(orderEntityList);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getOrder(@Valid @PathVariable("id") Long id) {
		try {
			OrderEntity orderEntity =  iOrderService.getOrderById(id);
			return ResponseEntity.ok(orderEntity);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateOrder(@Valid @PathVariable Long id,
			@RequestBody OrderDTO orderDTO) {
		try {
			OrderEntity orderEntity =  iOrderService.updateOrder(id ,orderDTO );
			return ResponseEntity.ok(orderEntity);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteOrder(@Valid @PathVariable Long id) {
		iOrderService.deleteOrder(id);
		
		return ResponseEntity.ok("delete order successfully with id:  " + id);
	}
	
	
	
}
