package canteen_bite.example.Canteen_bite.dto;
import canteen_bite.example.Canteen_bite.entity.Role;
import lombok.*;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AuthUserDetails {
    private Long id;
    private String email;
    private Role role;
}
