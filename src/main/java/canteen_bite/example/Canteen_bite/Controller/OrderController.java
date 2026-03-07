package canteen_bite.example.Canteen_bite.Controller;

import canteen_bite.example.Canteen_bite.dto.OrderRequest;
import canteen_bite.example.Canteen_bite.entity.Order;
import canteen_bite.example.Canteen_bite.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import canteen_bite.example.Canteen_bite.security.JwtService;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "${app.frontend.origin:http://localhost:3000}")
public class OrderController {
    private final OrderService orderService;
    private final JwtService jwtService;

    public OrderController(OrderService orderService, JwtService jwtService) {
        this.orderService = orderService;
        this.jwtService = jwtService;
    }

    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestBody OrderRequest req) {
        Order o = orderService.placeOrder(req);
        return ResponseEntity.ok(o);
    }

    @GetMapping
    public List<Order> all() { return orderService.getAllOrders(); }

    @GetMapping("/{id}")
    public ResponseEntity<Order> get(@PathVariable Long id) {
        return orderService.getById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(orderService.updateStatus(id, status));
    }

    @GetMapping("/status/{status}")
    public List<Order> byStatus(@PathVariable String status) { return orderService.getByStatus(status); }

    @GetMapping("/user")
    public List<Order> getUserOrders(@RequestHeader("Authorization") String token) {
        try {
            String userEmail = extractUserEmailFromToken(token);
            return orderService.getOrdersByUserEmail(userEmail);
        } catch (RuntimeException e) {
            throw new RuntimeException("Invalid token or user not found");
        }
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            String userEmail = extractUserEmailFromToken(token);
            Order cancelledOrder = orderService.cancelOrder(id, userEmail);
            return ResponseEntity.ok(cancelledOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    private String extractUserEmailFromToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7); // Remove "Bearer " prefix
            if (jwtService.validateToken(jwtToken)) {
                return jwtService.extractUsername(jwtToken);
            }
        }
        throw new RuntimeException("Invalid token");
    }
}
