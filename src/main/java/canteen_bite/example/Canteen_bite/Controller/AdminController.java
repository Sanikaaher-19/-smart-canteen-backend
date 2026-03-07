package canteen_bite.example.Canteen_bite.Controller;

import canteen_bite.example.Canteen_bite.dto.CreateUserRequest;
import canteen_bite.example.Canteen_bite.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "${app.frontend.origin:http://localhost:3000}")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * GET all orders filtered by time range and date
     */
    @GetMapping("/orders")
    public ResponseEntity<?> getOrders(
        @RequestParam(defaultValue = "day") String timeRange,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        logger.info("Fetching orders for timeRange: {}, date: {}", timeRange, date);
        try {
            return ResponseEntity.ok(adminService.getOrders(timeRange, date));
        } catch (Exception e) {
            logger.error("Error fetching orders", e);
            return ResponseEntity.status(500).body("Error fetching orders: " + e.getMessage());
        }
    }

    /**
     * GET all users with summary stats
     */
    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        logger.info("Fetching all users summary");
        try {
            return ResponseEntity.ok(adminService.getUsersSummary());
        } catch (Exception e) {
            logger.error("Error fetching users", e);
            return ResponseEntity.status(500).body("Error fetching users: " + e.getMessage());
        }
    }

    /**
     * GET revenue summary by time range
     */
    @GetMapping("/revenue")
    public ResponseEntity<?> getRevenue(
        @RequestParam(defaultValue = "day") String timeRange,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        logger.info("Fetching revenue for timeRange: {}, date: {}", timeRange, date);
        try {
            return ResponseEntity.ok(adminService.getRevenueSummary(timeRange, date));
        } catch (Exception e) {
            logger.error("Error fetching revenue", e);
            return ResponseEntity.status(500).body("Error fetching revenue: " + e.getMessage());
        }
    }

    /**
     * POST create a new user (admin only)
     */
    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest req) {
        logger.info("Admin attempting to create user with email: {}", req.getEmail());
        try {
            // Validate required fields
            if (req.getName() == null || req.getName().trim().isEmpty()
                    || req.getEmail() == null || req.getEmail().trim().isEmpty()
                    || req.getPassword() == null || req.getPassword().trim().isEmpty()
                    || req.getRole() == null || req.getRole().trim().isEmpty()) {
                logger.warn("Validation failed: Missing required fields");
                return ResponseEntity.badRequest().body("Name, email, password and role are required");
            }

            var result = adminService.createUser(req);
            logger.info("User created successfully with email: {}", req.getEmail());
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            logger.error("User creation failed", ex);
            return ResponseEntity.status(500).body("User creation failed: " + ex.getMessage());
        }
    }
}