package canteen_bite.example.Canteen_bite.repository;
import canteen_bite.example.Canteen_bite.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(String status);

}
