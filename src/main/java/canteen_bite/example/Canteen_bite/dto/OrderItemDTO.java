package canteen_bite.example.Canteen_bite.dto;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class OrderItemDTO {
    private Long menuItemId;
    private Integer quantity;
}
