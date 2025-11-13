package canteen_bite.example.Canteen_bite.service;

import canteen_bite.example.Canteen_bite.entity.MenuItem;
import canteen_bite.example.Canteen_bite.repository.MenuItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuService {
    private final MenuItemRepository repo;
    public MenuService(MenuItemRepository repo) { this.repo = repo; }

    public List<MenuItem> getAll() { return repo.findAll(); }
    public Optional<MenuItem> getById(Long id) { return repo.findById(id); }
    public MenuItem save(MenuItem item) { return repo.save(item); }
    public void delete(Long id) { repo.deleteById(id); }
    public List<MenuItem> getAvailable() { return repo.findByAvailableTrue(); }
}
