package canteen_bite.example.Canteen_bite.Controller;

import canteen_bite.example.Canteen_bite.dto.status;
import canteen_bite.example.Canteen_bite.service.KitchenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kitchen")
@CrossOrigin(origins = "${app.frontend.origin:https://smart-canteen-bite.netlify.app/}")
@PreAuthorize("hasAnyRole('KITCHEN_STAFF', 'ADMIN')")
public class KitchenController {

    private static final Logger logger = LoggerFactory.getLogger(KitchenController.class);
    private final KitchenService kitchenService;

    public KitchenController(KitchenService kitchenService) {
        this.kitchenService = kitchenService;
    }

    /**
     * GET all orders for kitchen staff
     * Shows: order ID, user name (not email), items, total, status, time, table
     */
    @GetMapping("/orders")
    public ResponseEntity<?> getOrders() {
        logger.info("Kitchen staff fetching all orders");
        try {
            return ResponseEntity.ok(kitchenService.getAllOrders());
        } catch (Exception e) {
            logger.error("Error fetching orders", e);
            return ResponseEntity.status(500).body("Error fetching orders: " + e.getMessage());
        }
    }

    /**
     * PATCH update order status
     * Allowed statuses: ACCEPTED, PREPARING, READY, COMPLETED
     */
    @PatchMapping("/orders/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(
        @PathVariable Long orderId,
        @RequestBody status req
    ) {
        String requestedStatus = req != null ? req.getStatus() : null;
        logger.info("Kitchen staff updating order {} to status {}", orderId, requestedStatus);
        try {
            var result = kitchenService.updateOrderStatus(orderId, requestedStatus);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error updating order status", e);
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }
}
