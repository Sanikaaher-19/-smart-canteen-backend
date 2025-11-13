package canteen_bite.example.Canteen_bite.repository;
import canteen_bite.example.Canteen_bite.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
