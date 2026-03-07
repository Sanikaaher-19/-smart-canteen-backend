package canteen_bite.example.Canteen_bite.repository;

import canteen_bite.example.Canteen_bite.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(String status);

    @Query("SELECT o FROM Order o WHERE o.user.email = :email")
    List<Order> findByUserEmail(@Param("email") String email);

    @Query("SELECT o FROM Order o WHERE o.orderTime BETWEEN :start AND :end")
    List<Order> findByOrderTimeBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}