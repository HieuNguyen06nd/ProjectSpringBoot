package com.hieunguyen.shopwq.service.imp;

import com.hieunguyen.shopwq.dto.RestaurantDto;
import com.hieunguyen.shopwq.model.Restaurant;
import com.hieunguyen.shopwq.model.User;
import com.hieunguyen.shopwq.request.RestaurantRequest;

import java.util.List;

public interface IRestaurantService {

    Restaurant createRestaurant(RestaurantRequest request, User user);

    Restaurant updateRestaurant(Long restaurantId, RestaurantRequest request) throws  Exception;

    void deleteRestaurant(Long restaurantId) throws  Exception;

    List<Restaurant> getAllRestaurant();

    List<Restaurant> searchRestaurant(String key);

    Restaurant findRestaurantById(Long id) throws Exception;

    Restaurant findRestaurantByUserId(Long userId) throws Exception;

    RestaurantDto addToFavorites(Long restaurantId, User user ) throws  Exception;

    Restaurant updateRestaurantStatus(Long id) throws Exception;
}
