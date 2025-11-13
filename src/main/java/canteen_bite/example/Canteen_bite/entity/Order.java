package canteen_bite.example.Canteen_bite.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Order {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tableNumber;
    private String status; // PENDING, PREPARING, READY, COMPLETED
    private LocalDateTime orderTime;
    private Double totalAmount;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)  // Explicit JoinColumn with nullable=true
    private User user; // who placed the order (optional)

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();
}
