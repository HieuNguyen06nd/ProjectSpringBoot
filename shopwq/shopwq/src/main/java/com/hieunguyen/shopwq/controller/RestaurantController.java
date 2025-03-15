package com.hieunguyen.shopwq.controller;

import com.hieunguyen.shopwq.dto.RestaurantDto;
import com.hieunguyen.shopwq.model.Restaurant;
import com.hieunguyen.shopwq.model.User;
import com.hieunguyen.shopwq.request.RestaurantRequest;
import com.hieunguyen.shopwq.service.UserService;
import com.hieunguyen.shopwq.service.imp.IRestaurantService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurant")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RestaurantController {

    IRestaurantService restaurantService;

    UserService userService;

    @GetMapping("/search")
    public ResponseEntity<List<Restaurant>> searchRestaurant(@RequestHeader("Authorization") String jwt,
                                                             @RequestParam String key) throws Exception {

        User user =userService.findUserByJwtToken(jwt);

        List<Restaurant> restaurant = restaurantService.searchRestaurant(key);

        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<Restaurant>> getAllRestaurant(@RequestHeader("Authorization") String jwt) throws Exception {

        User user =userService.findUserByJwtToken(jwt);

        List<Restaurant> restaurant = restaurantService.getAllRestaurant();

        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> findRestaurantById(@RequestHeader("Authorization") String jwt,
                                                            @PathVariable Long id) throws Exception {

        User user =userService.findUserByJwtToken(jwt);

        Restaurant restaurant = restaurantService.findRestaurantById(id);

        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    @GetMapping("/{id}/add-favorites")
    public ResponseEntity<RestaurantDto> addFavorites(@RequestHeader("Authorization") String jwt,
                                                         @PathVariable Long id) throws Exception {

        User user =userService.findUserByJwtToken(jwt);
        RestaurantDto restaurant = restaurantService.addToFavorites(id,user);

        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }


}
