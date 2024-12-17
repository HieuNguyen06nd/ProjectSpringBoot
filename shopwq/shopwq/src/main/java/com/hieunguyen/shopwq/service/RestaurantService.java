package com.hieunguyen.shopwq.service;

import com.hieunguyen.shopwq.dto.RestaurantDto;
import com.hieunguyen.shopwq.model.Address;
import com.hieunguyen.shopwq.model.Restaurant;
import com.hieunguyen.shopwq.model.User;
import com.hieunguyen.shopwq.repository.AddressRepository;
import com.hieunguyen.shopwq.repository.RestaurantRepository;
import com.hieunguyen.shopwq.repository.UserRepository;
import com.hieunguyen.shopwq.request.RestaurantRequest;
import com.hieunguyen.shopwq.service.imp.IRestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestaurantService implements IRestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final AddressRepository addressRepository;

    private final UserRepository userRepository;

    @Override
    public Restaurant createRestaurant(RestaurantRequest request, User user) {

        Address address = addressRepository.save(request.getAddress());

        Restaurant restaurant = new Restaurant();

        restaurant.setAddress(address);
        restaurant.setContactInformation(request.getContactInformation());
        restaurant.setCuisioneType(request.getCuisineType());
        restaurant.setDescription(request.getDescription());
        restaurant.setName(request.getName());
        restaurant.setOpeningHours(request.getOpeningHouse());
        restaurant.setImages(request.getImages());
        restaurant.setRegistrationonDate(LocalDateTime.now());
        restaurant.setOwner(user);


        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant updateRestaurant(Long restaurantId, RestaurantRequest request) throws Exception {

        Restaurant restaurant =findRestaurantById(restaurantId);

        if (restaurant.getCuisioneType() != null){
            restaurant.setCuisioneType(request.getCuisineType());
        }

        if (restaurant.getDescription() !=null){
            restaurant.setDescription(request.getDescription());
        }

        if (restaurant.getName() !=null){
            restaurant.setName(request.getName());
        }


        return restaurantRepository.save(restaurant);
    }

    @Override
    public void deleteRestaurant(Long restaurantId) throws Exception {

        Restaurant restaurant= findRestaurantById(restaurantId);

        if (restaurant != null){
            restaurantRepository.deleteById(restaurantId);
        }else {
            throw new Exception("Can not found restaurant with id: "+ restaurantId);
        }

    }

    @Override
    public List<Restaurant> getAllRestaurant() {
        return restaurantRepository.findAll();
    }

    @Override
    public List<Restaurant> searchRestaurant( String key) {
        return restaurantRepository.findBySearchQuery(key);
    }

    @Override
    public Restaurant findRestaurantById(Long id) throws Exception {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(id);

        if (restaurantOptional.isEmpty()){
            throw new Exception("Can not found restaurant with id: "+ id);
        }

        return restaurantOptional.get();
    }

    @Override
    public Restaurant findRestaurantByUserId(Long userId) throws Exception {

        Restaurant restaurant =restaurantRepository.findByOwnerId(userId);
        if (restaurant == null){
            throw new Exception("Can not found restaurant with owner id: "+ userId);
        }
        return restaurant;
    }

    @Override
    public RestaurantDto addToFavorites(Long restaurantId, User user) throws Exception {
        Restaurant restaurant = findRestaurantById(restaurantId);

        RestaurantDto restaurantDto = new RestaurantDto();
        restaurantDto.setDescription(restaurant.getDescription());
        restaurantDto.setImages(restaurant.getImages());
        restaurantDto.setTitle(restaurant.getName());
        restaurantDto.setId(restaurantId);

        if (user.getFavorites().contains(restaurantDto)) {
            user.getFavorites().remove(restaurantDto);
        } else {
            user.getFavorites().add(restaurantDto);
        }
        userRepository.save(user);

        return restaurantDto;
    }


    @Override
    public Restaurant updateRestaurantStatus(Long id) throws Exception {

        Restaurant restaurant = findRestaurantById(id);

        restaurant.setOpen(!restaurant.isOpen());


        return restaurantRepository.save(restaurant);
    }
}
