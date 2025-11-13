package canteen_bite.example.Canteen_bite.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderItem {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private MenuItem menuItem;

    private Integer quantity;
    private Double price; // snapshot price

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)  // Ensure not-null, required relation
    private Order order;
}
