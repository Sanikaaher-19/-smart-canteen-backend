package canteen_bite.example.Canteen_bite.dto;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class LoginRequest {
    private String email;
    private String password;
     private String role;

      public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
