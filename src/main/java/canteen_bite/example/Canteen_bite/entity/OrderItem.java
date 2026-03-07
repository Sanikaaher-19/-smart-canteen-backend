package canteen_bite.example.Canteen_bite.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore; // <-- ADD THIS LINE

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
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnore // <-- ADD THIS LINE
    private Order order;
}
