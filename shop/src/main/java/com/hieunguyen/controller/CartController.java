package com.hieunguyen.controller;

import com.hieunguyen.dto.request.AddItemRequest;
import com.hieunguyen.dto.response.ApiResponse;
import com.hieunguyen.entity.Cart;
import com.hieunguyen.entity.CartItem;
import com.hieunguyen.entity.Product;
import com.hieunguyen.entity.User;
import com.hieunguyen.service.CartItemService;
import com.hieunguyen.service.CartService;
import com.hieunguyen.service.ProductService;
import com.hieunguyen.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final CartItemService cartItemService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Cart> findUserCartHandler (@RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt);

        Cart cart = cartService.findUserCart(user);

        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PutMapping("/add")
    public ResponseEntity<CartItem> addItemToCart(@RequestBody AddItemRequest request,
                                                  @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Product product= productService.findProductById(request.getProductId());

        CartItem item = cartService.addCartItem(user, product, request.getSize(), request.getQuantity());

        ApiResponse response = new ApiResponse();
        response.setMessage("Item added to cart successfully");

        return new ResponseEntity<>(item, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItemHandler (@PathVariable Long cartItemId,
                                                              @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        cartItemService.deleteCartItem(user.getId(), cartItemId);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Item remove from cart");

        return new ResponseEntity<>(apiResponse, HttpStatus.ACCEPTED);
    }

    @PutMapping("/item/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItemHandler(@PathVariable Long cartItemId,
                                                            @RequestBody CartItem cartItem,
                                                            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        CartItem updateItem = null;

        if (cartItem.getQuantity() > 0){
            updateItem = cartItemService.updateCartItem(user.getId(), cartItemId, cartItem);
        }

        return new ResponseEntity<>(updateItem, HttpStatus.ACCEPTED);
    }
}
