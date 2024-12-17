package com.hieunguyen.shopwq.controller;

import com.hieunguyen.shopwq.model.Restaurant;
import com.hieunguyen.shopwq.model.User;
import com.hieunguyen.shopwq.reponse.MessageResponse;
import com.hieunguyen.shopwq.request.RestaurantRequest;
import com.hieunguyen.shopwq.service.UserService;
import com.hieunguyen.shopwq.service.imp.IRestaurantService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/restaurant")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminRestaurantController {

    IRestaurantService restaurantService;

    UserService userService;


    @PostMapping()
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody RestaurantRequest request,@RequestHeader("Authorization") String jwt) throws Exception {

        User user =userService.findUserByJwtToken(jwt);

        Restaurant restaurant = restaurantService.createRestaurant(request,user);

        return new ResponseEntity<>(restaurant, HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(@RequestBody RestaurantRequest request,
                                                       @RequestHeader("Authorization") String jwt,
                                                        @PathVariable Long id) throws Exception {

        User user =userService.findUserByJwtToken(jwt);

        Restaurant restaurant = restaurantService.updateRestaurant(id,request);

        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteRestaurant(@RequestBody RestaurantRequest request,
                                                       @RequestHeader("Authorization") String jwt,
                                                       @PathVariable Long id) throws Exception {

        User user =userService.findUserByJwtToken(jwt);

        restaurantService.deleteRestaurant(id);

        MessageResponse messageResponse = new MessageResponse();

        messageResponse.setMessage("Restaurant delete successfully with id: "+ id);

        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Restaurant> updateRestaurantStatus(@RequestBody RestaurantRequest request,
                                                                  @RequestHeader("Authorization") String jwt,
                                                                  @PathVariable Long id  ) throws Exception {

        User user =userService.findUserByJwtToken(jwt);

        Restaurant restaurant= restaurantService.updateRestaurantStatus(id);

        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<Restaurant> updateRestaurantStatus(@RequestBody RestaurantRequest request,
                                                             @RequestHeader("Authorization") String jwt ) throws Exception {

        User user =userService.findUserByJwtToken(jwt);

        Restaurant restaurant= restaurantService.findRestaurantByUserId(user.getId());

        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }
}
