package com.hieunguyen.controller;

import java.util.List;

import com.hieunguyen.exception.DataNotFoundException;
import com.hieunguyen.model.OrderDetailEntity;
import com.hieunguyen.reponse.OrderDetailResponse;
import com.hieunguyen.service.impl.IOrderServiceDetail;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class OrderDetailController {

	IOrderServiceDetail iOrderServiceDetail;


	@PostMapping("")
	public  ResponseEntity<?> CreateOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO,
			BindingResult result) {
		try {
			OrderDetailEntity orderDetailEntity = iOrderServiceDetail.createOrderDetail(orderDetailDTO);

			return ResponseEntity.ok(OrderDetailResponse.fromOrderDetail(orderDetailEntity));
		}catch ( Exception e){
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getOrderDetail(@Valid @PathVariable("id") Long id) {
		try {
			OrderDetailEntity orderDetailEntity = iOrderServiceDetail.getOrderDetailById(id);
			return ResponseEntity.ok(OrderDetailResponse.fromOrderDetail(orderDetailEntity));
//			return ResponseEntity.ok(orderDetailEntity);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
//	ds order detal 1 order
	@GetMapping("/order/{order_id}")
	public ResponseEntity<?> getOrderDetails(@Valid @PathVariable("order_id") Long order_id) {
		try {
			iOrderServiceDetail.getOrderfindId(order_id);
			List<OrderDetailEntity> orderDetailEntityList = iOrderServiceDetail.getOrderfindId(order_id);
			List<OrderDetailResponse> orderDetailResponseList = orderDetailEntityList.stream()
					.map(OrderDetailResponse::fromOrderDetail).toList();
			return ResponseEntity.ok(orderDetailResponseList);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateOrderDetail(@Valid @PathVariable Long id,
			@RequestBody OrderDetailDTO orderDetailDTO) throws DataNotFoundException {

		OrderDetailEntity orderDetailEntity= iOrderServiceDetail.updateOrderDetail(id, orderDetailDTO);
		return ResponseEntity.ok(orderDetailEntity);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteOrderDetail(@Valid @PathVariable Long id) {
		//TODO: process PUT request
		iOrderServiceDetail.deleteOrderDetail(id);
		return ResponseEntity.ok("delete order with id: " + id);
	}
	
	
}
