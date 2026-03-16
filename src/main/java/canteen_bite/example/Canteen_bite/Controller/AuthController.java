package canteen_bite.example.Canteen_bite.Controller;

import canteen_bite.example.Canteen_bite.dto.AuthResponse;
import canteen_bite.example.Canteen_bite.dto.LoginRequest;
import canteen_bite.example.Canteen_bite.dto.RegisterRequest;
import canteen_bite.example.Canteen_bite.entity.Role;
import canteen_bite.example.Canteen_bite.entity.User;
import canteen_bite.example.Canteen_bite.security.JwtService;
import canteen_bite.example.Canteen_bite.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
//@CrossOrigin(origins = "${app.frontend.origin:http://localhost:3000}")
public class AuthController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        try {
            // Only allow CUSTOMER role on public registration
            if (req.getRole() == null || !req.getRole().equalsIgnoreCase("CUSTOMER")) {
                return ResponseEntity.badRequest().body("Only customers can register.");
            }
            User u = userService.register(req);
            String token = jwtService.generateToken(u.getEmail());
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        return userService.findByEmail(req.getEmail())
            .map(user -> {
                if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
                    return ResponseEntity.status(401).body("Invalid credentials");
                }

                Role loginRole;
                try {
                    loginRole = Role.valueOf(req.getRole().toUpperCase());
                } catch (IllegalArgumentException | NullPointerException e) {
                    return ResponseEntity.status(403).body("Invalid role specified");
                }

                if (!user.getRole().equals(loginRole)) {
                    return ResponseEntity.status(403).body("User role does not match login role");
                }

                String token = jwtService.generateToken(user.getEmail());
                return ResponseEntity.ok(new AuthResponse(token));
            })
            .orElse(ResponseEntity.status(404).body("User not found"));
    }
}
