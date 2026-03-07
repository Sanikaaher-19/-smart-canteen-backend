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

    // store current status values like PLACED, ACCEPTED, PREPARING, READY, COMPLETED, CANCELLED
    private String status;

    @Column(name = "order_time")
    private LocalDateTime orderTime;

    @Column(name = "total_amount")
    private Double totalAmount;

    // time when order was cancelled (nullable)
    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user; // who placed the order (optional)

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();
}