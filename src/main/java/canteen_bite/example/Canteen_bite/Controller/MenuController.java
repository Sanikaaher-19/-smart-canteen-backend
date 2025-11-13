package canteen_bite.example.Canteen_bite.Controller;

import canteen_bite.example.Canteen_bite.entity.MenuItem;
import canteen_bite.example.Canteen_bite.service.MenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
@CrossOrigin(origins = "${app.frontend.origin:http://localhost:3000}")
public class MenuController {
    private final MenuService menuService;
    public MenuController(MenuService menuService) { this.menuService = menuService; }

    @GetMapping
    public List<MenuItem> all() { return menuService.getAll(); }

    @GetMapping("/available")
    public List<MenuItem> available() { return menuService.getAvailable(); }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> getOne(@PathVariable Long id) {
        return menuService.getById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Admin-only endpoints should be under /api/admin/** in SecurityConfig
    @PostMapping("/create")
    public MenuItem create(@RequestBody MenuItem item) { return menuService.save(item); }

    @PutMapping("/{id}")
    public ResponseEntity<MenuItem> update(@PathVariable Long id, @RequestBody MenuItem item) {
        return menuService.getById(id).map(existing -> {
            existing.setName(item.getName());
            existing.setDescription(item.getDescription());
            existing.setPrice(item.getPrice());
            existing.setCategory(item.getCategory());
            existing.setAvailable(item.getAvailable());
            return ResponseEntity.ok(menuService.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        menuService.delete(id);
        return ResponseEntity.ok().build();
    }
}
