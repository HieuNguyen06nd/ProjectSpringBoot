package com.hieunguyen.shopstorev2.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hieunguyen.shopstorev2.dto.response.ShippingStatusResponse;
import com.hieunguyen.shopstorev2.entities.Order;
import com.hieunguyen.shopstorev2.service.ShippingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service("ghnShippingService")
@RequiredArgsConstructor
@Slf4j
public class GHNShippingServiceImpl implements ShippingService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${ghn.token}")
    private String ghnToken;

    @Value("${ghn.shop-id}")
    private String ghnShopId;

    @Value("${ghn.base-url}")
    private String ghnBaseUrl;

    @Override
    public String createOrder(Order order) {
        String url = ghnBaseUrl + "/v2/shipping-order/create";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", ghnToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("ShopId", ghnShopId);

        Map<String, Object> body = new HashMap<>();
        body.put("order_code", "ORDER-" + order.getId());
        body.put("from_name", "Shop Store");
        body.put("from_phone", "0123456789");
        body.put("from_address", "Hà Nội");
        body.put("to_name", order.getUser().getUsername());
        body.put("to_phone", order.getUser().getPhone());
        body.put("to_address", order.getAddress().getDetail());
        body.put("weight", 1000);
        body.put("length", 20);
        body.put("width", 20);
        body.put("height", 10);
        body.put("service_id", 53320);
        body.put("payment_type_id", 1);
        body.put("required_note", "CHOXEMHANGKHONGTHU");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            return root.path("data").path("order_code").asText();
        } catch (Exception e) {
            log.error("GHN create order error", e);
            throw new RuntimeException("GHN error");
        }
    }

    @Override
    public ShippingStatusResponse trackOrder(String trackingCode) {
        String url = ghnBaseUrl + "/v2/shipping-order/track";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", ghnToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("order_code", trackingCode);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode status = root.path("data").path("status");
            return new ShippingStatusResponse(trackingCode, status.asText(), LocalDateTime.now());
        } catch (Exception e) {
            log.error("GHN track error", e);
            throw new RuntimeException("GHN track error");
        }
    }
}
