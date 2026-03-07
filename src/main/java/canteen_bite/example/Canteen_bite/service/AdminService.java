package canteen_bite.example.Canteen_bite.service;

import canteen_bite.example.Canteen_bite.dto.CreateUserRequest;
import canteen_bite.example.Canteen_bite.entity.Order;
import canteen_bite.example.Canteen_bite.entity.OrderItem;
import canteen_bite.example.Canteen_bite.entity.Role;
import canteen_bite.example.Canteen_bite.entity.User;
import canteen_bite.example.Canteen_bite.repository.OrderRepository;
import canteen_bite.example.Canteen_bite.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminService {

  private final OrderRepository orderRepo;
  private final UserRepository userRepo;
  private final PasswordEncoder passwordEncoder;

  public AdminService(OrderRepository orderRepo, UserRepository userRepo, PasswordEncoder passwordEncoder) {
    this.orderRepo = orderRepo;
    this.userRepo = userRepo;
    this.passwordEncoder = passwordEncoder;
  }

  /**
   * Get orders by time range and date
   */
  public List<Map<String, Object>> getOrders(String timeRange, LocalDate date) {
    LocalDate start = computeStart(timeRange, date);
    LocalDate end = computeEnd(timeRange, date);

    List<Order> orders = orderRepo.findByOrderTimeBetween(
      start.atStartOfDay(),
      end.plusDays(1).atStartOfDay()
    );

    return orders.stream().map(o -> {
      Map<String, Object> m = new HashMap<>();
      m.put("id", o.getId());
      m.put("customerEmail", o.getUser() != null ? o.getUser().getEmail() : "Guest");
      m.put("items", o.getItems().stream().map(item -> new HashMap<String, Object>() {{
        put("name", item.getMenuItem() != null ? item.getMenuItem().getName() : "Unknown");
        put("qty", item.getQuantity());
        put("price", item.getPrice());
      }}).collect(Collectors.toList()));
      m.put("totalAmount", o.getTotalAmount());
      m.put("status", o.getStatus());
      m.put("createdAt", o.getOrderTime());
      return m;
    }).collect(Collectors.toList());
  }

  /**
   * Get all users with summary stats
   */
  public List<Map<String, Object>> getUsersSummary() {
    List<User> users = userRepo.findAll();
    List<Map<String, Object>> out = new ArrayList<>();

    for (User u : users) {
      List<Order> userOrders = orderRepo.findByUserEmail(u.getEmail());
      long orderCount = userOrders.size();
      double totalSpent = userOrders.stream().mapToDouble(o -> o.getTotalAmount() != null ? o.getTotalAmount() : 0).sum();

      Map<String, Object> m = new HashMap<>();
      m.put("id", u.getId());
      m.put("name", u.getName());
      m.put("email", u.getEmail());
      m.put("role", u.getRole().toString());
      m.put("totalOrders", orderCount);
      m.put("totalSpent", totalSpent);
      m.put("createdAt", new Date());
      out.add(m);
    }
    return out;
  }

  /**
   * Get revenue summary by time range
   */
  public Map<String, Object> getRevenueSummary(String timeRange, LocalDate date) {
    LocalDate start = computeStart(timeRange, date);
    LocalDate end = computeEnd(timeRange, date);

    List<Order> orders = orderRepo.findByOrderTimeBetween(
      start.atStartOfDay(),
      end.plusDays(1).atStartOfDay()
    );

    double total = orders.stream()
      .mapToDouble(o -> o.getTotalAmount() != null ? o.getTotalAmount() : 0)
      .sum();
    int count = orders.size();
    double avg = count > 0 ? total / count : 0.0;

    // Top item aggregation
    Map<String, Integer> itemCount = new HashMap<>();
    for (Order o : orders) {
      if (o.getItems() != null) {
        for (OrderItem it : o.getItems()) {
          String itemName = it.getMenuItem() != null ? it.getMenuItem().getName() : "Unknown";
          itemCount.merge(itemName, it.getQuantity(), Integer::sum);
        }
      }
    }
    String topItem = itemCount.entrySet().stream()
      .max(Comparator.comparingInt(Map.Entry::getValue))
      .map(Map.Entry::getKey).orElse("—");

    // Daily breakdown
    Map<LocalDate, Map<String, Object>> details = new TreeMap<>();
    for (Order o : orders) {
      LocalDate d = o.getOrderTime().toLocalDate();
      details.computeIfAbsent(d, k -> {
        Map<String, Object> dd = new HashMap<>();
        dd.put("date", k.toString());
        dd.put("orderCount", 0);
        dd.put("revenue", 0.0);
        return dd;
      });
      Map<String, Object> dd = details.get(d);
      dd.put("orderCount", (int) dd.get("orderCount") + 1);
      dd.put("revenue", ((double) dd.get("revenue")) + (o.getTotalAmount() != null ? o.getTotalAmount() : 0));
    }

    Map<String, Object> res = new HashMap<>();
    res.put("total", total);
    res.put("orderCount", count);
    res.put("average", avg);
    res.put("topItem", topItem);
    res.put("details", new ArrayList<>(details.values()));
    return res;
  }

  /**
   * Create a new user (admin only)
   */
  @Transactional
  public Map<String, Object> createUser(CreateUserRequest req) {
    // Check if user exists
    if (userRepo.findByEmail(req.getEmail()).isPresent()) {
      throw new RuntimeException("User already exists with this email");
    }

    // Map role string to Role enum
    Role role;
    try {
      role = Role.valueOf(req.getRole().toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new RuntimeException("Invalid role: " + req.getRole());
    }

    User u = User.builder()
      .name(req.getName())
      .email(req.getEmail().toLowerCase().trim())
      .password(passwordEncoder.encode(req.getPassword()))
      .role(role)
      .build();

    userRepo.save(u);
    return Collections.singletonMap("message", "User created successfully");
  }

  /**
   * Compute start date based on time range
   */
  private LocalDate computeStart(String range, LocalDate date) {
    switch (range.toLowerCase()) {
      case "week":
        return date.with(java.time.DayOfWeek.MONDAY);
      case "month":
        return date.with(TemporalAdjusters.firstDayOfMonth());
      default:
        return date;
    }
  }

  /**
   * Compute end date based on time range
   */
  private LocalDate computeEnd(String range, LocalDate date) {
    switch (range.toLowerCase()) {
      case "week":
        return date.with(java.time.DayOfWeek.SUNDAY);
      case "month":
        return date.with(TemporalAdjusters.lastDayOfMonth());
      default:
        return date;
    }
  }
}