package canteen_bite.example.Canteen_bite.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
// Remove @AllArgsConstructor as it conflicts with your usage
public class AuthResponse {
    private String token;
    private String tokenType = "Bearer";

    // Manually defined constructor that accepts only the token
    public AuthResponse(String token) {
        this.token = token;
        // tokenType defaults to "Bearer"
    }
}