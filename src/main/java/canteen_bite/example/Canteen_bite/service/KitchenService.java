package canteen_bite.example.Canteen_bite.service;

import canteen_bite.example.Canteen_bite.entity.Order;
import canteen_bite.example.Canteen_bite.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class KitchenService {

    private final OrderRepository orderRepo;
    private static final Set<String> TERMINAL_STATUSES = Set.of("COMPLETED", "CANCELLED");
    private static final Set<String> ALLOWED_TARGET_STATUSES = Set.of(
            "ACCEPTED", "PREPARING", "READY", "COMPLETED"
    );

    public KitchenService(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    /**
     * Get all orders with relevant kitchen info
     * Returns: id, user name (NOT email), items, total, status, orderTime, tableNumber
     */
    public List<Map<String, Object>> getAllOrders() {
        List<Order> orders = orderRepo.findAll();
        
        return orders.stream().map(order -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", order.getId());
            m.put("customerName", order.getUser() != null ? order.getUser().getName() : "Guest");
            m.put("tableNumber", order.getTableNumber());
            m.put("status", order.getStatus());
            m.put("orderTime", order.getOrderTime());
            m.put("totalAmount", order.getTotalAmount());
            
            // Map items with details
            m.put("items", order.getItems().stream().map(item -> {
                Map<String, Object> itemMap = new HashMap<>();
                itemMap.put("name", item.getMenuItem() != null ? item.getMenuItem().getName() : "Unknown Item");
                itemMap.put("quantity", item.getQuantity());
                itemMap.put("price", item.getPrice());
                return itemMap;
            }).collect(Collectors.toList()));
            
            // Add timestamps for cancel detection
            m.put("createdAt", order.getOrderTime());
            m.put("cancelledAt", null); // adjust if you have a cancelledAt field in Order
            
            return m;
        }).collect(Collectors.toList());
    }

    /**
     * Update order status (ACCEPTED, PREPARING, READY, COMPLETED)
     */
    @Transactional
    public Map<String, Object> updateOrderStatus(Long orderId, String newStatus) {
        Order order = orderRepo.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));

        String current = normalizeStatus(order.getStatus());
        String next = normalizeStatus(newStatus);

        if (next == null || !ALLOWED_TARGET_STATUSES.contains(next)) {
            throw new RuntimeException("Invalid target status: " + newStatus);
        }

        if (TERMINAL_STATUSES.contains(current)) {
            throw new RuntimeException("Cannot update a " + current + " order");
        }

        if (!isTransitionAllowed(current, next)) {
            throw new RuntimeException("Invalid status transition: " + current + " -> " + next);
        }

        order.setStatus(next);
        orderRepo.save(order);

        Map<String, Object> res = new HashMap<>();
        res.put("id", order.getId());
        res.put("status", order.getStatus());
        res.put("message", "Order status updated to " + next);
        return res;
    }

    private String normalizeStatus(String value) {
        if (value == null) return null;
        String normalized = value.trim().toUpperCase();
        if (normalized.isBlank()) return null;
        if ("PENDING".equals(normalized)) return "PLACED";
        if ("READY_FOR_PICKUP".equals(normalized)) return "READY";
        return normalized;
    }

    private boolean isTransitionAllowed(String current, String next) {
        return switch (current) {
            case "PLACED" -> "ACCEPTED".equals(next);
            case "ACCEPTED" -> "PREPARING".equals(next);
            case "PREPARING" -> "READY".equals(next);
            case "READY" -> "COMPLETED".equals(next);
            default -> false;
        };
    }
}
