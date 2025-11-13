package canteen_bite.example.Canteen_bite.service;
import canteen_bite.example.Canteen_bite.dto.AuthUserDetails;
import canteen_bite.example.Canteen_bite.entity.User;
import canteen_bite.example.Canteen_bite.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;
@Service
public class AuthUserService {
    private final UserRepository userRepository;

    public AuthUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<AuthUserDetails> loadByEmail(String email) {
        Optional<User> u = userRepository.findByEmail(email);
        return u.map(user -> new AuthUserDetails(user.getId(), user.getEmail(), user.getRole()));
    }
}
