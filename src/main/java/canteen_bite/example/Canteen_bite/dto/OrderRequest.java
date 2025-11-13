package canteen_bite.example.Canteen_bite.dto;
import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class OrderRequest {
    private String tableNumber;
    private List<OrderItemDTO> items;
    private String customerEmail; // optional: link to user
}
