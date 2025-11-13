package canteen_bite.example.Canteen_bite.service;

import canteen_bite.example.Canteen_bite.dto.OrderItemDTO;
import canteen_bite.example.Canteen_bite.dto.OrderRequest;
import canteen_bite.example.Canteen_bite.entity.MenuItem;
import canteen_bite.example.Canteen_bite.entity.Order;
import canteen_bite.example.Canteen_bite.entity.OrderItem;
import canteen_bite.example.Canteen_bite.repository.MenuItemRepository;
import canteen_bite.example.Canteen_bite.repository.OrderRepository;
import canteen_bite.example.Canteen_bite.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepo;
    private final MenuItemRepository menuRepo;
    private final UserRepository userRepo;

    public OrderService(OrderRepository orderRepo, MenuItemRepository menuRepo, UserRepository userRepo) {
        this.orderRepo = orderRepo;
        this.menuRepo = menuRepo;
        this.userRepo = userRepo;
    }

    @Transactional
    public Order placeOrder(OrderRequest request) {
        // Validate inputs
        if (request.getTableNumber() == null || request.getTableNumber().isBlank()) {
            throw new IllegalArgumentException("Table number is required");
        }
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order items cannot be empty");
        }

        Order order = new Order();
        order.setTableNumber(request.getTableNumber());
        order.setOrderTime(LocalDateTime.now());
        order.setStatus("PENDING");

        if (request.getCustomerEmail() != null) {
            userRepo.findByEmail(request.getCustomerEmail()).ifPresent(order::setUser);
        }

        List<OrderItem> items = new ArrayList<>();
        double total = 0.0;

        for (OrderItemDTO dto : request.getItems()) {
            MenuItem mi = menuRepo.findById(dto.getMenuItemId())
                    .orElseThrow(() -> new RuntimeException("MenuItem not found: " + dto.getMenuItemId()));

            OrderItem oi = new OrderItem();
            oi.setMenuItem(mi);
            oi.setQuantity(dto.getQuantity());
            oi.setPrice(mi.getPrice() * dto.getQuantity());

            oi.setOrder(order);  // Set back reference to parent order
            items.add(oi);

            total += oi.getPrice();
        }

        order.setItems(items);
        order.setTotalAmount(total);

        return orderRepo.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    public Optional<Order> getById(Long id) {
        return orderRepo.findById(id);
    }

    public Order updateStatus(Long id, String status) {
        Order o = orderRepo.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        o.setStatus(status);
        return orderRepo.save(o);
    }

    public List<Order> getByStatus(String status) {
        return orderRepo.findByStatus(status);
    }

    @Transactional
    public Order cancelOrder(Long orderId, String userEmail) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Check if the user owns the order
        if (order.getUser() == null || !order.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("You can only cancel your own orders");
        }

        // Check if order is in a cancellable state (e.g., PENDING or PREPARING)
        if (!"PENDING".equals(order.getStatus()) && !"PREPARING".equals(order.getStatus())) {
            throw new RuntimeException("Order cannot be cancelled at this stage");
        }

        // Check 5-minute window
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(order.getOrderTime().plusMinutes(5))) {
            throw new RuntimeException("Order can only be cancelled within 5 minutes of placement");
        }

        // Update status to CANCELLED
        order.setStatus("CANCELLED");
        Order savedOrder = orderRepo.save(order);

        // Log notification for kitchen staff
        System.out.println("Order " + orderId + " cancelled by user " + userEmail + ". Notify kitchen staff: Order has been cancelled.");

        return savedOrder;
    }
}
