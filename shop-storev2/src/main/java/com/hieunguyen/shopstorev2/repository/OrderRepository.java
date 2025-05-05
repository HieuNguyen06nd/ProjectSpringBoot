package com.hieunguyen.shopstorev2.repository;

import com.hieunguyen.shopstorev2.entities.Order;
import com.hieunguyen.shopstorev2.entities.User;
import com.hieunguyen.shopstorev2.utils.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Trả về tất cả đơn hàng của user
    List<Order> findByUser(User user);

    // Có thể thêm các phương thức khác tùy vào nhu cầu như theo trạng thái, thời gian
    List<Order> findByUserAndStatusAndDeletedFalse(User user, OrderStatus status);

    List<Order> findByUserOrderByCreatedAtDesc(User user);
}
