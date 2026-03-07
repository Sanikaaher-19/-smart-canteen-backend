package canteen_bite.example.Canteen_bite.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequest {
    private String name;
    private String email;
    private String password;
    private String role; // "customer", "kitchen_staff", "admin"
}