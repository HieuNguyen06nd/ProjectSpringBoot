package com.hieunguyen.shopwq.request;

import com.hieunguyen.shopwq.model.Address;
import com.hieunguyen.shopwq.model.ContactInformation;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RestaurantRequest {
    private  Long id;

    private String name;

    private String description;

    private String cuisineType;

    private Address address;

    private ContactInformation contactInformation;

    private String openingHouse;

    private List<String> images;


}
