package canteen_bite.example.Canteen_bite.Controller;

import canteen_bite.example.Canteen_bite.dto.RegisterRequest;
import canteen_bite.example.Canteen_bite.entity.User;
import canteen_bite.example.Canteen_bite.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "${app.frontend.origin:http://localhost:3000}")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    // Admin-only create user endpoint for KITCHEN_STAFF or ADMIN roles
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody RegisterRequest req) {
        try {
            logger.info("Attempt to create user with email: {}", req.getEmail());

            // Validate required fields
            if (req.getName() == null || req.getName().trim().isEmpty()
                    || req.getEmail() == null || req.getEmail().trim().isEmpty()
                    || req.getPassword() == null || req.getPassword().trim().isEmpty()) {
                logger.warn("Validation failed: Missing required fields");
                return ResponseEntity.badRequest().body("Name, email and password are required");
            }

            // Validate role
            if (req.getRole() == null
                    || (!req.getRole().equalsIgnoreCase("KITCHEN_STAFF") && !req.getRole().equalsIgnoreCase("ADMIN"))) {
                logger.warn("Validation failed: Invalid role {}", req.getRole());
                return ResponseEntity.badRequest().body("Only 'KITCHEN_STAFF' or 'ADMIN' roles allowed");
            }

            User newUser = userService.register(req);
            logger.info("User created successfully with email: {}", newUser.getEmail());
            return ResponseEntity.ok("User created successfully");
        } catch (Exception ex) {
            logger.error("User creation failed", ex);
            return ResponseEntity.status(500).body("User creation failed: " + ex.getMessage());
        }
    }
}
