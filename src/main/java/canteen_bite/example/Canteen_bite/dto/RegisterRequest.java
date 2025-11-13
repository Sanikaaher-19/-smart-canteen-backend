package canteen_bite.example.Canteen_bite.dto;
//import canteen_bite.example.Canteen_bite.entity.Role;
import lombok.*;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String role; 
}
